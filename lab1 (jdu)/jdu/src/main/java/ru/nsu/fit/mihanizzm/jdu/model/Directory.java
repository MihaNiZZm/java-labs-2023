package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;
import java.util.List;


// CR: toString
final public class Directory extends DuFile {
    private final List<DuFile> children;

    public List<DuFile> getChildren() {
        return children;
    }

    public Directory(Path path, long size, String name, List<DuFile> newChildren) {
        super(path, size, name);
        children = newChildren;
    }
}
