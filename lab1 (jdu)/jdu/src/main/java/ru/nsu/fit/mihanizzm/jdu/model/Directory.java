package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;
import java.util.List;

public final class Directory extends DuFile {
    private final List<DuFile> children;

    public List<DuFile> getChildren() {
        return children;
    }

    public Directory(Path path, long size, List<DuFile> newChildren) {
        super(path, size);
        children = newChildren;
    }

    @Override
    public String toString() {
        return "<Directory>\n\n" +
                "File size: " +
                this.getSize() +
                "\n" +
                "File path: " +
                this.getPath() +
                "\n" +
                "Children files: " +
                this.children +
                "\n\n";
    }
}
