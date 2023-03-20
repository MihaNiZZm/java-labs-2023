package ru.nsu.fit.mihanizzm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class CommandLineParser {
    static final Path DEFAULT_ROOT_PATH = Path.of(System.getProperty("user.dir"));
    static final int DEFAULT_DEPTH = 3;
    static final int DEFAULT_LIMIT = 5;
    static final boolean IS_NOT_CHECKING_SYMLINKS = false;

    static private int getArgValue(int index, List<String> argsList) throws CommandLineArgsException {
        int argValue = 0;
        try {
            argValue = Integer.parseInt(argsList.get(index));
        }
        catch (NumberFormatException error) {
            System.err.println("NumberFormatException: \"" + error.getMessage() + "\"\n");
        }
        if (argValue <= 0) {
            throw new CommandLineArgsException("Invalid value of a depth.");
        }
        return argValue;
    }
    static CommandLineOptions getCmdOptions(String[] Args) throws CommandLineArgsException {
        List<String> argsList = Arrays.asList(Args);
        int depth = DEFAULT_DEPTH;
        int limit = DEFAULT_LIMIT;
        boolean isCheckingSymLinks = IS_NOT_CHECKING_SYMLINKS;
        Path rootFilePath = DEFAULT_ROOT_PATH;

        if (Args.length == 0) {
            return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
        }

        if (argsList.contains("--depth")) {
            int index = argsList.indexOf("--depth") + 1;
            depth = CommandLineParser.getArgValue(index, argsList);
        }

        if (argsList.contains("--limit")) {
            int index = argsList.indexOf("--limit") + 1;
            limit = CommandLineParser.getArgValue(index, argsList);
        }

        if (argsList.contains("-L")) {
            isCheckingSymLinks = true;
        }

        if (Files.exists(Path.of(argsList.get(argsList.size() - 1)))) {
            rootFilePath = Path.of(argsList.get(argsList.size() - 1));
        }

        return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
    }
}
