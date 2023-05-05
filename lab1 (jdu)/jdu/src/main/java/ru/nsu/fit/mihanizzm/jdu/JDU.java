package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.DuFile;

public class JDU {
    public static void main(String[] args) {
        try {
            CommandLineOptions opts = CommandLineParser.getCmdOptions(args);
            DuFile root = DuTreeBuilder.buildFileTree(opts);
            DuPrinter.print(root, opts, System.out);
        }
        catch (DuException exception) {
            System.err.println(exception.getMessage());
            if (exception instanceof CommandLineArgumentsException) {
                System.out.println(CommandLineParser.showUsage());
            }
        }
    }
}