package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public abstract sealed class DuFile permits Directory, RegularFile, SymLink {
    private final Path path;
    private final long size;
    private final String name;
    // CR: move to printer
    private final int depth;
    // CR: move to printer
    private final boolean isCheckingSymLinks;
    // CR: redundant, use size
    private boolean hasUnknownSize;

    public long getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public int getDepth() {
        return this.depth;
    }

    public Path getPath() { return  this.path; }

    public boolean getCheckingLinks() { return this.isCheckingSymLinks; }

    public boolean getHasUnknownSize() { return this.hasUnknownSize; }

    public void setHasUnknownSize(boolean res) { this.hasUnknownSize = res; }

    @Override
    public int hashCode() {
        int result = 0;
        result += 31 * path.toString().hashCode();
        result += 31 * size;
        result += 31 * name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DuFile)) {
            return false;
        }

        return (this.size != ((DuFile) obj).size) &&
               (this.path != ((DuFile) obj).path);
    }



    protected DuFile(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        this.path = path;
        this.size = size;
        this.name = name;
        this.depth = depth;
        this.isCheckingSymLinks = isCheckingSymLinks;
    }
}

