package ru.nsu.fit.mihanizzm;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;

public class JDU {
    static final int ZERO_DEPTH = 0;
    private static DuFile buildFileTree(CommandLineOptions options) throws IOException {
        DuFile root = null;

        if (Files.isDirectory(options.rootPath())) {
            root = new Directory(options.rootPath(), ZERO_DEPTH, options.isCheckingSymLinks());
        }
        else if (Files.isRegularFile(options.rootPath())) {
            root = new File(options.rootPath(), ZERO_DEPTH, options.isCheckingSymLinks());
        }
        else if (Files.isSymbolicLink(options.rootPath())) {
            root = new SymLink(options.rootPath(), ZERO_DEPTH, options.isCheckingSymLinks());
        }

        if (root instanceof SymLink && options.isCheckingSymLinks()) {
            root = ((SymLink) root).resolve();
        }

        return root;
    }
    public static void main(String[] args) {
        try {
            HashSet<DuFile> visitedFiles = new HashSet<>();
            CommandLineOptions opts = CommandLineParser.getCmdOptions(args);
            DuFile root = buildFileTree(opts);
            System.out.println(DuPrinter.getPrintInfo(root, opts, visitedFiles));
        }
        catch (IOException exception) {
            System.err.println("IOException Error occurred! Error message: \"" + exception.getMessage() + "\"\n");
        }
        catch (DuException exception) {
            System.err.println("DuException Error occurred! Error message: \"" + exception.getMessage() + "\"\n");
        }
    }
}