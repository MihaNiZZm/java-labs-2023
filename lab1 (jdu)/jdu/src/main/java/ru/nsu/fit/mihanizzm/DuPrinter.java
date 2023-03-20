package ru.nsu.fit.mihanizzm;

import java.io.IOException;
import java.util.HashSet;

public class DuPrinter {
    final static long KIBIBYTE = 1024;
    final static long MEBIBYTE = KIBIBYTE * 1024;
    final static long GIBIBYTE = MEBIBYTE * 1024;
    final static long TEBIBYTE = GIBIBYTE * 1024;

    // ccr: mb make it using swich case?
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

    public static String getPrintInfo(DuFile file, CommandLineOptions opts, HashSet<DuFile> visited) throws IOException {
        if (file.getDepth() > opts.depth() || visited.contains(file)) {
            return "";
        }
        visited.add(file);

        StringBuilder resultString = new StringBuilder();
        resultString.append("\t".repeat(Math.max(0, file.getDepth())));

        if (file instanceof File) {
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

            for (DuFile child: Directory.getLimitedChildren((Directory) file, opts.limit())) {
                resultString.append(getPrintInfo(child, opts, visited));
            }
        }
        return resultString.toString();
    }
}
