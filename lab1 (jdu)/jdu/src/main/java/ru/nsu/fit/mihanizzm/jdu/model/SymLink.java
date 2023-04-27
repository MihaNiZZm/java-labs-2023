package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public final class SymLink extends DuFile {
    private final Path realPath;
//  CR:  private final DuFile realFile;

    public Path getRealPath() {
        return this.realPath;
    }

    public SymLink(Path path, long size, String name, Path realPath) {
        super(path, size, name);
        this.realPath = realPath;
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
                "File name: " +
                this.getName() +
                "\n" +
                "File real path: " +
                this.getRealPath() +
                "\n\n";
    }
}
