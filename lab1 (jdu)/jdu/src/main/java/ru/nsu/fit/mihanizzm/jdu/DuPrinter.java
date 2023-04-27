package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.*;

import java.io.PrintStream;
import java.util.HashSet;

public class DuPrinter {
    private final CommandLineOptions options;
    private final PrintStream printStream;
    private final HashSet<DuFile> visitedFiles;
    private int currentDepth;
    private boolean isFromSymLink;

    public DuPrinter(CommandLineOptions opts, PrintStream printStream) {
        this.options = opts;
        this.printStream = printStream;

        this.visitedFiles = new HashSet<>();
        this.currentDepth = 0;
        this.isFromSymLink = false;
    }

//    public static void print(DuFile root, CommandLineOptions opts, PrintStream printStream) {
//
//    }

    enum Size {
        KB("KiB", 1024),
        MB("MiB", KB.maxSize * 1024),
        GB("GiB", MB.maxSize * 1024),
        TB("TiB", GB.maxSize * 1024);

        private final String suffix;
        private final long maxSize;

        Size(String suffix, long maxSize) {
            this.suffix = suffix;
            this.maxSize = maxSize;
        }

        static String convert(long size) {
            if (size < 1024) {
                return String.format("%dB", size);
            }
            for (Size sz : Size.values()) {
                if (size < sz.maxSize * 1024) {
                    return String.format("%.2f" + sz.suffix, size / (double) sz.maxSize);
                }
            }
            return String.format("%.2f" + Size.TB.suffix, size / (double) TB.maxSize);
        }
    }

    /*


   CR:

    foo
      slink1 -> bar

    bar
      slink2 -> foo

    depth = 5

    foo
      slink1 -> bar
        bar
          slink2 -> foo


     */
    public void getPrintInfo(DuFile file) {
        if (printStream == null) {
            throw new PrinterException("Print stream was not set. Can't print to null print stream.");
        }
        if (currentDepth > options.depth() || !visitedFiles.add(file)) {
            printStream.append("");
            return;
        }

        String stringSize = file.hasUnknownSize() ? "unknown" : Size.convert(file.getSize());
        printStream.append("\t".repeat(currentDepth));

        switch (file) {
            case RegularFile regularFile -> {
                if (isFromSymLink) {
                    printStream.append("Followed symlink: ");
                    isFromSymLink = false;
                }

                printStream.append(regularFile.getName()).append(" [").append(stringSize).append("]");
                if (stringSize.equals("unknown")) {
                    printStream.append(" (file has unknown size)");
                }
                printStream.append("\n");
            }
            case SymLink symLink -> {
                if (options.isCheckingSymLinks()) {
                    isFromSymLink = true;
                    getPrintInfo(DuTreeBuilder.resolveSymLink(symLink));
                }
                else {
                    printStream.append("Unfollowed symlink: ").append(symLink.getName()).append(" [").append(stringSize).append("]\n");
                }
            }
            case Directory directory -> {
                if (isFromSymLink) {
                    printStream.append("Followed symlink: ");
                    isFromSymLink = false;
                }

                printStream.append(directory.getName()).append(" [").append(stringSize).append("]\n");

                if (directory.getChildren() == null) {
                    return;
                }

                ++currentDepth;
                for (DuFile child: DuTreeBuilder.getLimitedNumberOfChildrenFiles(directory, options.limit())) {
                    getPrintInfo(child);
                }
                --currentDepth;
            }
            case UnknownFile unknownFile -> {
                if (isFromSymLink) {
                    printStream.append("Followed symlink: ");
                    isFromSymLink = false;
                }

                printStream.append(unknownFile.getName()).append(" [").append(stringSize).append("]").append(" (file is unknown)\n");
            }
        }
    }
}
