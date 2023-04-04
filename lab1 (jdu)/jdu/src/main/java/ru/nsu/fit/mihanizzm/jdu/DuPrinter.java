package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.Directory;
import ru.nsu.fit.mihanizzm.jdu.model.DuFile;
import ru.nsu.fit.mihanizzm.jdu.model.RegularFile;
import ru.nsu.fit.mihanizzm.jdu.model.SymLink;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DuPrinter {
    private static final long KIBIBYTE = 1024;
    private static final long MEBIBYTE = KIBIBYTE * 1024;
    private static final long GIBIBYTE = MEBIBYTE * 1024;
    private static final long TEBIBYTE = GIBIBYTE * 1024;

//    CR:
//    enum Size {
//        KB("KiB", 1024),
//        MB,
//        GB,
//        TB;
//
//        private final String suffix;
//        private final int maxSize;
//
//        Size(String suffix, int maxSize) {
//            this.suffix = suffix;
//            this.maxSize = maxSize;
//        }
//
//        static String convert(long size) {
//            for (Size sz : Size.values()) {
//                if (size < sz.maxSize) {
//                    return String.format("%.2f" + sz.suffix, size);
//                }
//            }
//            // ???
//            return null;
//        }
//    }
    private static String convertSize(long size) {
        assert size >= 0;
        if (size < KIBIBYTE) {
            return String.format("%dB", size);
        }
        else if (size < MEBIBYTE) {
            double tmpSize = (double)size / (KIBIBYTE);
            return String.format("%.2fKiB", tmpSize);
        }
        else if (size < GIBIBYTE) {
            double tmpSize = (double)size / (MEBIBYTE);
            return String.format("%.2fMiB", tmpSize);
        }
        else if (size < TEBIBYTE) {
            double tmpSize = (double)size / (GIBIBYTE);
            return String.format("%.2fGiB", tmpSize);
        }
        else {
            double tmpSize = (double)size / (TEBIBYTE);
            return String.format("%.2fTiB", tmpSize);
        }
    }

    // CR: non-static, add field 'printStream'
    private static String getPrintInfo(DuFile file, CommandLineOptions opts, HashSet<DuFile> visited) {
        if (file.getDepth() > opts.depth() || !visited.add(file)) {
            return "";
        }

        StringBuilder resultString = new StringBuilder();
        String stringSize = file.getHasUnknownSize() ? "unknown" : convertSize(file.getSize());
        resultString.append("\t".repeat(file.getDepth()));

        switch (file) {
            case RegularFile ignored -> {
                resultString.append(file.getName()).append(" [").append(stringSize).append("]");
                if (stringSize.equals("unknown")) {
                    resultString.append(" (file couldn't be accessed or has unknown size.)");
                }
                resultString.append("\n");
            }
            case SymLink symLink -> {
                if (opts.isCheckingSymLinks()) {
                    resultString.append("Followed symlink: ").append(getPrintInfo(DuTreeBuilder.resolveSymLink(symLink), opts, visited));
                }
                else {
                    resultString.append("Unfollowed symlink: ").append(symLink.getName()).append(" [").append(convertSize(symLink.getSize())).append("]\n");
                }
            }
            case Directory directory -> {
                List<DuFile> childrenList = DuTreeBuilder.createChildren(directory);
                directory.setChildren(childrenList);

                resultString.append(directory.getName()).append(" [").append(convertSize(directory.getSize())).append("]\n");

                for (DuFile child: getLimitedChildren(directory, opts.limit())) {
                    resultString.append(getPrintInfo(child, opts, visited));
                }
            }
        }
        return resultString.toString();
    }

    // CR: only public method
    public static void print(PrintStream printStream, DuFile file, CommandLineOptions opts, HashSet<DuFile> visited) {
        printStream.append(getPrintInfo(file, opts, visited));
    }

    public static List<DuFile> getLimitedChildren(Directory dir, int limit) {
        List<DuFile> unsortedChildren = dir.getChildren();
        unsortedChildren.sort(Comparator.comparingLong(DuFile::getSize).reversed());
        return unsortedChildren.subList(0, Math.min(limit, unsortedChildren.size()));
    }
}
