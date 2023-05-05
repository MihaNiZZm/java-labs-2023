package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class SymLink extends DuFile {
    private Path realPath;
    private DuFile realFile;
    public Path getRealPath() {
        return this.realPath;
    }

    public DuFile getRealFile() { return realFile; }

    public void setRealFile(DuFile realFile) { this.realFile = realFile; }

    public void setRealPath(Path realPath) { this.realPath = realPath; }

    public SymLink(Path path, long size, Path realPath, DuFile realFile) {
        super(path, size);
        this.realPath = realPath;
        this.realFile = realFile;
    }

    @Override
    public String toString() {
        return "<SymLink>\n\n" +
                "File size: " +
                this.getSize() +
                "\n" +
                "File path: " +
                this.getPath() +
                "\n" +
                "File real path: " +
                this.getRealPath() +
                "\n\n";
    }
}
