package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.*;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DuPrinter {
    private final CommandLineOptions options;
    private final PrintStream printStream;
    // CR: do we really need two?
    private final HashSet<DuFile> visitedFiles;
    private final HashSet<DuFile> resolvedVisitedFiles;
    private int currentDepth;
    private boolean isInSymlink;

    private DuPrinter(CommandLineOptions opts, PrintStream printStream) {
        this.options = opts;
        this.printStream = printStream;

        this.resolvedVisitedFiles = new HashSet<>();
        this.visitedFiles = new HashSet<>();
        this.currentDepth = 0;
        this.isInSymlink = false;
    }

    public static void print(DuFile root, CommandLineOptions opts, PrintStream printStream) {
        DuPrinter printer = new DuPrinter(opts, printStream);
        printer.getPrintInfo(root, false);
    }

    public static List<DuFile> getLimitedNumberOfChildrenFiles(Directory dir, int limit) {
        // CR: stream
        List<DuFile> unsortedChildren = dir.getChildren();
        unsortedChildren.sort(Comparator.comparingLong(DuFile::getSize).reversed());
        return unsortedChildren.subList(0, Math.min(limit, unsortedChildren.size()));
    }

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
    public void getPrintInfo(DuFile file, boolean isResolved) {
        if (printStream == null) {
            throw new PrinterException("Print stream was not set. Can't print to null print stream.");
        }

        if (currentDepth > options.depth()) {
            return;
        }

        if (isInSymlink) {
            if (!resolvedVisitedFiles.add(file)) {
                return;
            }
        } else {
            if (!visitedFiles.add(file)) {
                return;
            }
        }



        String stringSize = file.hasUnknownSize() ? "unknown" : Size.convert(file.getSize());

        switch (file) {
            case RegularFile regularFile -> {
                printStream.append("\t".repeat(currentDepth));
                if (isResolved) {
                    printStream.append("Followed symlink: ");
                }

                printStream.append(regularFile.getPath().getFileName().toString()).append(" [").append(stringSize).append("]");
                if (stringSize.equals("unknown")) {
                    printStream.append(" (file has unknown size)");
                }
                printStream.append("\n");
            }
            case SymLink symLink -> {
                if (options.isCheckingSymLinks()) {

                    isInSymlink = true;
                    getPrintInfo(symLink.getRealFile(), true);
                    isInSymlink = false;
                }
                else {
                    printStream.append("\t".repeat(currentDepth));
                    printStream.append("Unfollowed symlink: ").append(symLink.getPath().getFileName().toString()).append(" [").append(stringSize).append("]\n");
                }
            }
            case Directory directory -> {
                printStream.append("\t".repeat(currentDepth));
                if (isResolved) {
                    printStream.append("Followed symlink: ");
                }

                printStream.append(directory.getPath().getFileName().toString()).append(" [").append(stringSize).append("]\n");

                if (directory.getChildren() == null || directory.getChildren().size() == 0) {
                    return;
                }

                ++currentDepth;
                for (DuFile child: getLimitedNumberOfChildrenFiles(directory, options.limit())) {
                    getPrintInfo(child, false);
                }
                --currentDepth;
            }
            case UnknownFile unknownFile -> {
                printStream.append("\t".repeat(currentDepth));
                if (isResolved) {
                    printStream.append("Followed symlink: ");
                }

                printStream.append(unknownFile.getPath().toString()).append(" [").append(stringSize).append("]").append(" (file is unknown)\n");
            }
        }
    }
}
