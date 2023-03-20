package ru.nsu.fit.mihanizzm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// CR: sealed class
public abstract sealed class DuFile {
    protected Path path;
    protected long size;
    protected String name;
    protected final int depth;
    protected boolean isCheckingSymLinks; // Question. May it be static for DuFile class? //ccr: why not? :)

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



    protected DuFile(Path path, int depth, boolean isCheckingSymLinks) {
        this.path = path;
        // CR: do it before constructor
        try {
            this.size = getSizeOfFile(path);
        }
        // foo 10GB
        //   bar 10GB
        //   f [unknown]
        // Couldn't access 'f' file: cause
        catch (IOException error) {
            // CR: log problem and show to user
            this.size = 0;
        }
        this.name = getNameOfFile(path);
        this.depth = depth;
        this.isCheckingSymLinks = isCheckingSymLinks;
    }

    private static String getNameOfFile(Path filePath) {
        return filePath.getFileName().toString();
    }

    // CR: move tree building to separate class TreeBuilder
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
        else {
            String errorMessage = "Cannot get size of file: \"" + filePath + "\"\n";
            throw new FileAnalysisException(errorMessage);
        }
        return size;
    }
}

final class RegularFile extends DuFile {

    public RegularFile(Path path, int depth, boolean isCheckingSymLinks) {
        super(path, depth, isCheckingSymLinks);
    }
}

final class Directory extends DuFile {
    private List<DuFile> children;

    public List<DuFile> getChildren() {
        return children;
    }
    public Directory(Path path, int depth, boolean isCheckingSymLinks) throws IOException {
        super(path, depth, isCheckingSymLinks);
        children = createChildren(depth, isCheckingSymLinks);
    }

    // CR: move tree building to separate class TreeBuilde
    private List<DuFile> createChildren(int depth, boolean isCheckingSymLinks) throws IOException {
        children = new ArrayList<>();
        for (Path child: Files.list(path).toList()) {
            if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                Directory childDirectory = new Directory(child, depth + 1, isCheckingSymLinks);
                children.add(childDirectory);
            }
            if (Files.isRegularFile(child, LinkOption.NOFOLLOW_LINKS)) {
                RegularFile childRegularFile = new RegularFile(child, depth + 1, isCheckingSymLinks);
                children.add(childRegularFile);
            }
            if (Files.isSymbolicLink(child)) {
                SymLink childSymLink = new SymLink(child, depth + 1, isCheckingSymLinks);
                children.add(childSymLink);
            }
            // CR: exception for unknown type
        }
        return children;
    }
}

final class SymLink extends DuFile {
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
            return new RegularFile(realPath, depth, isCheckingSymLinks);
        }
        if (Files.isSymbolicLink(realPath)) {
            SymLink nestedLink = new SymLink(realPath, depth, isCheckingSymLinks);
            return nestedLink.resolve();
        }
        return null;
    }
}
