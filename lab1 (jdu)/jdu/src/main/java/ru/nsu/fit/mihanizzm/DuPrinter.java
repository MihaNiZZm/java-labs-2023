package ru.nsu.fit.mihanizzm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DuPrinter {
    // CR: check private and final everywhere
    final static long KIBIBYTE = 1024;
    final static long MEBIBYTE = KIBIBYTE * 1024;
    final static long GIBIBYTE = MEBIBYTE * 1024;
    final static long TEBIBYTE = GIBIBYTE * 1024;

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

    // ccr: mb make it using switch case?
    // author comment: switch doesn't work with variables with type of long.
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

    public static String getPrintInfo(DuFile file, CommandLineOptions opts, HashSet<DuFile> visited) throws IOException {
        // CR: contains & add -> add
        if (file.getDepth() > opts.depth() || visited.contains(file)) {
            return "";
        }
        visited.add(file);

        StringBuilder resultString = new StringBuilder();
        resultString.append("\t".repeat(Math.max(0, file.getDepth())));

        // CR: switch expression
        if (file instanceof RegularFile) {
            // CR: depth field in printer
            resultString.append(file.getName()).append(" [").append(convertSize(file.getSize())).append("]\n");
         }
        else if (file instanceof SymLink) {
            resultString.append("SymLink: ");
            if (opts.isCheckingSymLinks()) {
                resultString.append(getPrintInfo(((SymLink) file).resolve(), opts, visited));
            }
            else {
                resultString.append(file.getName()).append(" [").append(convertSize(file.getSize())).append("]\n");
            }
        }
        else if (file instanceof Directory) {
            resultString.append(file.getName()).append(" [").append(convertSize(file.getSize())).append("]\n");

            for (DuFile child: getLimitedChildren((Directory) file, opts.limit())) {
                resultString.append(getPrintInfo(child, opts, visited));
            }
        }
        return resultString.toString();
    }

    static public List<DuFile> getLimitedChildren(Directory dir, int limit) {
        List<DuFile> unsortedChildren = dir.getChildren();
        unsortedChildren.sort((o1, o2) -> (int)(o2.getSize() - o1.getSize()));
        // CR :unsortedChildren.subList
        List<DuFile> nChildren = new ArrayList<>();
        for (int i = 0; i < limit; ++i) {
            if (i >= unsortedChildren.size()) {
                break;
            }
            nChildren.add(unsortedChildren.get(i));
        }
        return nChildren;
    }
}
