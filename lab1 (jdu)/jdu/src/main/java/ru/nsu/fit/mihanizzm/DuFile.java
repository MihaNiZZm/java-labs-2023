package ru.nsu.fit.mihanizzm;

import java.nio.file.Path;
import java.util.List;

// CR: sealed class
public abstract sealed class DuFile {
    private final Path path;
    private final long size;
    private final String name;
    private final int depth;
    private final boolean isCheckingSymLinks;
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

final class RegularFile extends DuFile {
    public RegularFile(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        super(path, size, name, depth, isCheckingSymLinks);
    }
}

final class Directory extends DuFile {
    private List<DuFile> children;

    public List<DuFile> getChildren() {
        return children;
    }

    public void setChildren(List<DuFile> childrenList) { this.children = childrenList; }

    public Directory(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        super(path, size, name, depth, isCheckingSymLinks);
    }
}

final class SymLink extends DuFile {
    private Path realPath;

    public Path getRealPath() { return this.realPath; }

    public void setRealPath(Path realPath) { this.realPath = realPath; }

    public SymLink(Path path, long size, String name, int depth, boolean isCheckingSymLinks) {
        super(path, size, name, depth, isCheckingSymLinks);
    }
}
