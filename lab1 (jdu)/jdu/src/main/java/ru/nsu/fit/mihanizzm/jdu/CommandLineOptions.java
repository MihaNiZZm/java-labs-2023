package ru.nsu.fit.mihanizzm.jdu;

import java.nio.file.Path;

public record CommandLineOptions(Path rootPath, int limit, int depth, boolean isCheckingSymLinks) {}
