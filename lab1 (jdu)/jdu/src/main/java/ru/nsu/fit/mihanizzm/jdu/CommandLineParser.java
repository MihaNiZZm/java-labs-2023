package ru.nsu.fit.mihanizzm.jdu;

import java.nio.file.Files;
import java.nio.file.Path;

public class CommandLineParser {
    static final private Path DEFAULT_ROOT_PATH = Path.of(System.getProperty("user.dir"));
    static final private int DEFAULT_DEPTH = 3;
    static final private int DEFAULT_LIMIT = 5;
    static final private boolean IS_NOT_CHECKING_SYMLINKS = false;

    static private int getArgValue(int index, String[] args) throws CommandLineException {
        int argValue;
        if (index + 1 == args.length) {
            throw new NoArgumentValueException("The option \"" + args[index] + "\" is the last argument but this option requires an integer value after it.");
        }
        try {
            argValue = Integer.parseInt(args[index + 1]);
        }
        catch (NumberFormatException error) {
            throw new ArgumentNumericalException("Could not parse value of a \"" + args[index] + "\" option. Error message: \"" + error.getMessage() + "\"");
        }
        if (argValue <= 0) {
            throw new InvalidArgumentValueException("Invalid value of a \"" + args[index] + "\" option.");
        }
        return argValue;
    }

    static CommandLineOptions getCmdOptions(String[] args) throws CommandLineException {
        int index = 0;
        int depth = DEFAULT_DEPTH;
        int limit = DEFAULT_LIMIT;
        boolean isCheckingSymLinks = IS_NOT_CHECKING_SYMLINKS;
        Path rootFilePath = DEFAULT_ROOT_PATH;

        if (args.length == 0) {
            return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
        }

        while (index != args.length) {
            if (args[index].equals("--depth")) {
                depth = getArgValue(index, args);
                index += 2;
            } else if (args[index].equals("--limit")) {
                limit = getArgValue(index, args);
                index += 2;
            } else if (args[index].equals("-L")) {
                isCheckingSymLinks = true;
                index += 1;
            } else if (index == args.length - 1) {
                if (Files.exists(Path.of(args[index]))) {
                    rootFilePath = Path.of(args[index]);
                } else {
                    throw new UnknownFileException("The file on this path: \"" + args[index] + "\" does not exist or is not available.");
                }
                index += 1;
            } else {
                throw new UnknownArgumentException("The option \"" + args[index] + "\" is unknown.");
            }
        }
        return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
    }
}
