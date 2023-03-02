package ru.nsu.fit.mihanizzm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.mihanizzm.JDU.getNameOfFile;
import static ru.nsu.fit.mihanizzm.JDU.getSizeOfFile;

public abstract class DuFile {
    protected Path path;
    protected long size;
    protected String name;
    protected int depth;
    protected boolean isCheckingSymLinks;

    abstract long getSize() throws IOException;

    protected DuFile(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        this.path = path;
        this.size = getSizeOfFile(path);
        this.name = getNameOfFile(path);
        this.depth = depth;
        this.isCheckingSymLinks = isCheckingSymLinks;
    }
}

class File extends DuFile {
    @Override
    long getSize() throws IOException {
        return Files.size(path);
    }
    public File(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
    }
}

class Directory extends DuFile {
    private List<DuFile> children;

    @Override
    long getSize() throws IOException {
        long size = 0;
        for (DuFile file: children) {
           size += file.getSize();
        }
        return size;
    }

    public Directory(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
        children = new ArrayList<>();
        for (Path child: Files.list(path).toList()) {
            if (Files.isDirectory(child)) {
                children.add(new Directory(child, depth - 1, isCheckingSymLinks));
            }
        }
    }
}

class SymLink extends DuFile {
    private Path realPath;
    @Override
    long getSize() throws IOException {
        return isCheckingSymLinks ? getSizeOfFile(realPath) : getSizeOfFile(path);
    }

    public SymLink(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
        realPath = path.toRealPath();
    }
}
