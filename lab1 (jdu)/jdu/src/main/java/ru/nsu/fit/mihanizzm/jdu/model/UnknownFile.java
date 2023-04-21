package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class UnknownFile extends DuFile {

    public UnknownFile(Path path, long size, String name) {
        super(path, size, name);
        size = -1;
    }
}
