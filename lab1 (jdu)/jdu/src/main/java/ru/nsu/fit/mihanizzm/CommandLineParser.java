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
    static CommandLineOptions getCmdOptions(String[] Args) {
        List<String> ArgsList = Arrays.asList(Args);
        int depth = DEFAULT_DEPTH;
        int limit = DEFAULT_LIMIT;
        boolean isCheckingSymLinks = IS_NOT_CHECKING_SYMLINKS;
        Path rootFilePath = DEFAULT_ROOT_PATH;

        if (Args.length == 0) {
            return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
        }

        if (ArgsList.contains("--depth")) {
            int index = ArgsList.indexOf("--depth") + 1;
            int argValue = 0;
            try {
                argValue = Integer.parseInt(ArgsList.get(index));
            }
            catch (NumberFormatException error) {
                System.err.println(error);
            }
            if (argValue <= 0) {
                throw new CommandLineArgsException("Invalid value of a depth.");
            }
            depth = argValue;
        }

        if (ArgsList.contains("--limit")) {
            int index = ArgsList.indexOf("--limit") + 1;
            int argValue = 0;
            try {
                argValue = Integer.parseInt(ArgsList.get(index));
            }
            catch (NumberFormatException error) {
                System.err.println(error);
            }
            if (argValue <= 0) {
                throw new CommandLineArgsException("Invalid value of a depth.");
            }
            limit = argValue;
        }

        if (ArgsList.contains("-L")) {
            isCheckingSymLinks = true;
        }

        if (Files.exists(Path.of(ArgsList.get(ArgsList.size() - 1)))) {
            rootFilePath = Path.of(ArgsList.get(ArgsList.size() - 1));
        }

        return new CommandLineOptions(rootFilePath, limit, depth, isCheckingSymLinks);
    }
}
