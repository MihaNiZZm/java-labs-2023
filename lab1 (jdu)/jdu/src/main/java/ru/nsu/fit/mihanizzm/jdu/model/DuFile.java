package ru.nsu.fit.mihanizzm.jdu.model;

import java.nio.file.Path;

public abstract sealed class DuFile permits Directory, RegularFile, SymLink, UnknownFile {
    private final Path path;
    private final long size;

    public boolean hasUnknownSize() {
        return size == -1;
    }

    public long getSize() {
        return this.size;
    }

    public Path getPath() { return this.path; }

    @Override
    public int hashCode() {
        int result = 0;
        result += 31 * path.toString().hashCode();
        result += 31 * size;
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

        return (this.size == ((DuFile) obj).size) &&
               (this.path.equals(((DuFile) obj).path));
    }

    protected DuFile(Path path, long size) {
        this.path = path;
        this.size = size;
    }

    @Override
    public String toString() {
        return "<DuFile>\n\n" +
                "File size: " +
                this.size +
                "\n" +
                "File path: " +
                this.path +
                "\n\n";
    }
}

