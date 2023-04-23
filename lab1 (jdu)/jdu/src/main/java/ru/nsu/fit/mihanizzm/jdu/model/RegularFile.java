package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class RegularFile extends DuFile {
    public RegularFile(Path path, long size, String name) {
        super(path, size, name);
    }

    @Override
    public String toString() {
        return "<RegularFile>\n\n" +
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
