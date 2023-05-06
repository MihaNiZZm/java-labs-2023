package ru.nsu.fit.mihanizzm.jdu;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class CommandLineParser {
    private static final Path DEFAULT_ROOT_PATH = Path.of(System.getProperty("user.dir"));
    private static final int DEFAULT_DEPTH = 3;
    private static final int DEFAULT_LIMIT = 5;
    private static final boolean NOT_CHECKING = false;

    // CR: rename
    private static int parseValue(String[] args, int index) {
        int argValue;
        try {
            // CR: Integer.parseUnsignedInt?
            argValue = Integer.parseInt(args[index + 1]);
        }
        catch (NumberFormatException error) {
            throw new CommandLineArgumentsException("Could not parse value of a \"" + args[index] + "\" option. Error message: \"" + error.getMessage() + "\"");
        }
        if (argValue < 0) {
            throw new CommandLineArgumentsException("Invalid value of a \"" + args[index] + "\" option.");
        }
        return argValue;
    }

    // CR: rename
    private static int getArgValue(int index, String[] args) throws CommandLineArgumentsException {
        int argValue;
        if (index + 1 >= args.length) {
            throw new CommandLineArgumentsException("The option \"" + args[index] + "\" is the last argument but this option requires an integer value after it.");
        }
        argValue = parseValue(args, index);
        return argValue;
    }

    /**
     * The method gets args from command line and parses it to options that will be used in a program further.
     * Default parameters values are: <ul>
     *     <li>depth = 3</li>
     *     <li>limit = 5</li>
     *     <li>isCheckingLinks = false</li>
     *     <li>rootPath = Path.of(System.getProperty("user.dir"))</li>
     * </ul>
     * More info about default values and usage of options: {@link #showUsage()}.
     * @param args an array of String variables that describe options a user want to create.
     * @return an instance of CommandLineOptions record which has 'rootPath', 'limit', 'depth' and 'isCheckingLinks' fields.
     * @throws CommandLineArgumentsException if the arguments are invalid (depth or limit are < 0 or rootPath doesn't exist).
     */
    public static CommandLineOptions getCmdOptions(String[] args) throws CommandLineArgumentsException {
        int index = 0;
        int depth = DEFAULT_DEPTH;
        int limit = DEFAULT_LIMIT;
        boolean isCheckingSymLinks = NOT_CHECKING;
        Path rootFilePath = DEFAULT_ROOT_PATH;

        if (args.length == 0) {
            return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
        }

        label:
        while (index != args.length) {
            switch (args[index]) {
                case "--depth" -> {
                    depth = getArgValue(index, args);
                    index += 2;
                }
                case "--limit" -> {
                    limit = getArgValue(index, args);
                    index += 2;
                }
                case "-L" -> {
                    isCheckingSymLinks = true;
                    index += 1;
                }
                default -> {
                    StringBuilder compositePathString = new StringBuilder();
                    compositePathString.append(args[index]);
                    while (index != args.length - 1) {
                        ++index;
                        compositePathString.append(" ").append(args[index]);
                    }
                    Path compositePath;
                    try {
                        compositePath = Path.of(compositePathString.toString());
                    } catch (InvalidPathException error) {
                        throw new CommandLineArgumentsException("Path " + compositePathString + "is invalid.");
                    }
                    if (Files.exists(compositePath)) {
                        rootFilePath = compositePath;
                        break label;
                    } else {
                        throw new CommandLineArgumentsException("Path " + compositePathString + " doesn't exist or it's an unknown command line argument.");
                    }
                }
            }
        }
        return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
    }

    public static String showUsage() {
    return """
            Usage:
            '--limit n' - shows maximum n heaviest files in each directory. n - an integer number. Default n is 5
            '--depth n' - shows maximum n levels of depth of a root directory. n - an integer number.
            Default value for n is 3. Example: root directory: 'D:/'. Imagine you have file with path 'D:/dir1/dir2/dir3/dir4/file'.
            Then you can see a file only if your depth value is 5 or higher. If you depth value is 3 you will see all directories and files until dir3 level.
            If you have 0 depth you will only see size of a root directory without files it contains.
            '-L' - follows real paths of symbolic links. False by default.
            The path must be the last thing you print in command line for correct work of the program (you should write it after all options e.g. --limit, --depth, -L).
            The path may contain any amount of spaces in it.
            """;
    }
}
