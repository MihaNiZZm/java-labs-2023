package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.DuFile;

import java.util.HashSet;

public class JDU {
    public static void main(String[] args) {
        try {
            CommandLineOptions opts = CommandLineParser.getCmdOptions(args);
            DuFile root = DuTreeBuilder.buildFileTree(opts);
            // CR: move to printer
            HashSet<DuFile> visitedFiles = new HashSet<>();
            DuPrinter.print(System.out, root, opts, visitedFiles);
        }
        catch (DuException exception) {
            System.err.println(exception.getMessage());
            if (exception instanceof CommandLineException) {
                // CR: separate method
                System.out.println("""
                        Usage:
                        '--limit n' - show maximum n heaviest files in each directory. n - an integer number. Default n is 5
                        '--depth n' - show maximum n levels of depth of a root directory. n - an integer number.
                        Default value for n is 3. Example: root directory: 'D:/'. Imagine you have file with path 'D:/dir1/dir2/dir3/dir4/file'.
                        Then you can see a file only if your depth value is 5 or higher. If you depth value is 3 you will see all directories and files until dir3 level.
                        If you have 0 depth you will only see size of a root directory without files it contains.
                        '-L' - go throw symbolic links' real path. False by default.
                        The last argument of your command line is your root path. The path can not contain spaces in it's name.
                        Default root path is the path from which you launched the program.
                        """);
            }
        }
    }
}