package ru.nsu.fit.mihanizzm;

import java.nio.file.Path;
public record CommandLineOptions(Path rootPath, int limit, int depth, boolean isCheckingSymLinks) {}
