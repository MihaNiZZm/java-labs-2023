package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class UnknownFile extends DuFile {

    public UnknownFile(Path path, long size, String name) {
        super(path, size, name);
    }

    @Override
    public String toString() {
        return "<UnknownFile>\n\n" +
                "File size: " +
                this.getSize() +
                "\n" +
                "File path: " +
                this.getPath() +
                "\n" +
                "File name: " +
                this.getName() +
                "\n\n";
    }
}
