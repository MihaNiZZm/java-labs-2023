package ru.nsu.fit.mihanizzm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class DuFile {
    protected Path path;
    protected long size;
    protected String name;
    protected int depth;
    protected boolean isCheckingSymLinks; // Question. May it be static for DuFile class?

    public long getSize() {
        return this.size;
    }
    public String getName() {
        return this.name;
    }
    public int getDepth() {
        return this.depth;
    }

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



    protected DuFile(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        this.path = path;
        this.size = getSizeOfFile(path);
        this.name = getNameOfFile(path);
        this.depth = depth;
        this.isCheckingSymLinks = isCheckingSymLinks;
    }

    private static String getNameOfFile(Path filePath) {
        return filePath.getFileName().toString();
    }
    private static long getSizeOfFile(Path filePath) throws IOException {
        long size = 0;
        if (Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
            List<Path> folderFiles = Files.list(filePath).toList();
            for (Path file: folderFiles) {
                if (Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) {
                    size += getSizeOfFile(file);
                }
                else if (Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
                    size += Files.size(file);
                }
                else if (Files.isSymbolicLink(file)) {
                    size += Files.size(file);
                }
            }
        }
        else if (Files.isRegularFile(filePath, LinkOption.NOFOLLOW_LINKS)) {
            size = Files.size(filePath);
        }
        else if (Files.isSymbolicLink(filePath)) {
            size = SymLink.SYMLINK_SIZE; // Question. What should I do with size of symbolic links?
        }
        return size;
    }
}

class File extends DuFile {

    public File(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
    }
}

class Directory extends DuFile {
    private List<DuFile> children;

    static public List<DuFile> getLimitedChildren(Directory dir, int limit) {
        List<DuFile> rawChildren = dir.getChildren();
        rawChildren.sort((o1, o2) -> (int)(o2.getSize() - o1.getSize()));

        List<DuFile> sortedChildren = new ArrayList<>();
        for (int i = 0; i < limit; ++i) {
            if (i >= rawChildren.size()) {
                break;
            }
            sortedChildren.add(rawChildren.get(i));
        }
        return sortedChildren;
    }
    public List<DuFile> getChildren() {
        return children;
    }
    public Directory(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
        children = createChildren(depth, isCheckingSymLinks);
    }

    private List<DuFile> createChildren(int depth, boolean isCheckingSymLinks) throws IOException {
        children = new ArrayList<>();
        for (Path child: Files.list(path).toList()) {
            if (Files.isDirectory(child)) {
                Directory childDirectory = new Directory(child, depth + 1, isCheckingSymLinks);
                children.add(childDirectory);
            }
            if (Files.isRegularFile(child)) {
                File childFile = new File(child, depth + 1, isCheckingSymLinks);
                children.add(childFile);
            }
            if (Files.isSymbolicLink(child)) {
                SymLink childSymLink = new SymLink(child, depth + 1, isCheckingSymLinks);
                children.add(childSymLink);
            }
        }
        return children;
    }
}

class SymLink extends DuFile {
    final private Path realPath;
    static final int SYMLINK_SIZE = 0;

    public SymLink(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
        size = SYMLINK_SIZE;
        realPath = path.toRealPath();
    }

    public DuFile resolve() throws IOException {
        if (Files.isDirectory(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return new Directory(realPath, depth, isCheckingSymLinks);
        }
        if (Files.isRegularFile(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return new File(realPath, depth, isCheckingSymLinks);
        }
        if (Files.isSymbolicLink(realPath)) {
            SymLink nestedLink = new SymLink(realPath, depth, isCheckingSymLinks);
            return nestedLink.resolve();
        }
        return null;
    }
}
