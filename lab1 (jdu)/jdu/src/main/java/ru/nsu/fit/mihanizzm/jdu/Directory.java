package ru.nsu.fit.mihanizzm.jdu;

import java.nio.file.Path;
import java.util.List;

final public class Directory extends DuFile {
    private List<DuFile> children;

    public List<DuFile> getChildren() {
        return children;
    }

    void setChildren(List<DuFile> childrenList) {
        this.children = childrenList;
    }

    public Directory(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        super(path, size, name, depth, isCheckingSymLinks);
    }

    public Directory(Path path, long size, String name, int depth, boolean isCheckingSymLinks, List<DuFile> newChildren) {
        super(path, size, name, depth, isCheckingSymLinks);
        children = newChildren;
    }
}
