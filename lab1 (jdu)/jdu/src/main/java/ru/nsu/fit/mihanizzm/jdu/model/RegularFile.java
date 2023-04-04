package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class RegularFile extends DuFile {
    public RegularFile(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        super(path, size, name, depth, isCheckingSymLinks);
    }

    public RegularFile(Path path) {
        super(path, 0, "", 0, false);
    }
}
