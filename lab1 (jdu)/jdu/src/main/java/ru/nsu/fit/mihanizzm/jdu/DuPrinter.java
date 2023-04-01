package ru.nsu.fit.mihanizzm.jdu;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;

public class DuPrinter {
    final private static long KIBIBYTE = 1024;
    final private static long MEBIBYTE = KIBIBYTE * 1024;
    final private static long GIBIBYTE = MEBIBYTE * 1024;
    final private static long TEBIBYTE = GIBIBYTE * 1024;

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

    // foo
    //   symlink

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

                for (DuFile child: DuTreeBuilder.getLimitedChildren(directory, opts.limit())) {
                    resultString.append(getPrintInfo(child, opts, visited));
                }
            }
        }
        return resultString.toString();
    }

    public static void printInfoInStream(PrintStream printStream, DuFile file, CommandLineOptions opts, HashSet<DuFile> visited) {
        printStream.append(getPrintInfo(file, opts, visited));
    }
}
