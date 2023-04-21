package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class RegularFile extends DuFile {
    public RegularFile(Path path, long size, String name) {
        super(path, size, name);
    }
}
