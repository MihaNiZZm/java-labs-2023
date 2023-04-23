package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;
import java.util.List;

final public class Directory extends DuFile {
    private final List<DuFile> children;

    public List<DuFile> getChildren() {
        return children;
    }

    public Directory(Path path, long size, String name, List<DuFile> newChildren) {
        super(path, size, name);
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
                "File name: " +
                this.getName() +
                "\n" +
                "Children files: " +
                this.children +
                "\n\n";
    }
}
