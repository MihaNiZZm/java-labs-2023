package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

final public class SymLink extends DuFile {
    private Path realPath;

    public Path getRealPath() {
        return this.realPath;
    }

    public void setRealPath(Path realPath) {
        this.realPath = realPath;
    }

    public SymLink(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        super(path, size, name, depth, isCheckingSymLinks);
    }
}
