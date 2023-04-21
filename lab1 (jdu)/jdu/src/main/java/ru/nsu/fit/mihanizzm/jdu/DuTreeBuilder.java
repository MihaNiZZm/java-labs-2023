package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DuTreeBuilder {
    static private final int UNKNOWN_FILE_SIZE = -1;
    static private final int DEFAULT_SYMLINK_SIZE = 0;

    private static Directory getNewDirectory(Path rootPath) {
        long size = 0;

        List<DuFile> childrenFiles;
        try {
            childrenFiles = createChildren(rootPath);
        }
        catch (TreeBuilderException error) {
            return new Directory(rootPath, 0, getNameOfFile(rootPath), null);
        }

        for (DuFile childFile : childrenFiles) {
            if (!childFile.hasUnknownSize()) {
                size += childFile.getSize();
            }
        }

        return new Directory(rootPath, size, getNameOfFile(rootPath), childrenFiles);
    }

    private static RegularFile getNewRegularFile(Path rootPath) {
        long size;

        try {
            size = Files.size(rootPath);
        }
        catch (IOException error) {
            size = UNKNOWN_FILE_SIZE;
        }

        return new RegularFile(rootPath, size, getNameOfFile(rootPath));
    }

    private static SymLink getNewSymLink(Path rootPath) {
        return new SymLink(rootPath, DEFAULT_SYMLINK_SIZE, getNameOfFile(rootPath));
    }

    private static UnknownFile getNewUnknownFile(Path rootPath) {
        return new UnknownFile(rootPath, UNKNOWN_FILE_SIZE, getNameOfFile(rootPath));
    }

    public static DuFile buildFileTree(CommandLineOptions options) {
        if (Files.isDirectory(options.rootPath())) {
            return getNewDirectory(options.rootPath());
        } else if (Files.isRegularFile(options.rootPath())) {
            return getNewRegularFile(options.rootPath());
        } else if (Files.isSymbolicLink(options.rootPath())) {
            return getNewSymLink(options.rootPath());
        } else {
            return getNewUnknownFile(options.rootPath());
        }
    }

    private static String getNameOfFile(Path filePath) {
        return filePath.getFileName().toString();
    }

    private static List<DuFile> createChildren(Path dirPath) {
        List<DuFile> children = new ArrayList<>();
        List<Path> childrenList;
        try {
            childrenList = Files.list(dirPath).toList();
        }
        catch (IOException error) {
            throw new TreeBuilderException("Can't get the list of files in directory on path: \"" + dirPath + "\". List of files is can't be accessed or it's not a directory.");
        }

        for (Path child: childrenList) {
            if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                Directory childDirectory = getNewDirectory(child);
                children.add(childDirectory);
            } else if (Files.isRegularFile(child, LinkOption.NOFOLLOW_LINKS)) {
                RegularFile childRegularFile = getNewRegularFile(child);
                children.add(childRegularFile);
            } else if (Files.isSymbolicLink(child)) {
                SymLink childSymLink = getNewSymLink(child);
                children.add(childSymLink);
            } else {
                UnknownFile unknownFile = getNewUnknownFile(child);
                children.add(unknownFile);
            }

        }
        return children;
    }

    public static DuFile resolveSymLink(SymLink symlink) {
        Path realPath = symlink.getRealPath();

        if (Files.isDirectory(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return getNewDirectory(realPath);
        } else if (Files.isRegularFile(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return getNewRegularFile(realPath);
        } else if (Files.isSymbolicLink(realPath)) {
            SymLink nestedLink = getNewSymLink(realPath);
            return resolveSymLink(nestedLink);
        } else {
            return getNewUnknownFile(realPath);
        }
    }

    public static List<DuFile> getLimitedNumberOfChildrenFiles(Directory dir, int limit) {
        List<DuFile> unsortedChildren = dir.getChildren();
        unsortedChildren.sort(Comparator.comparingLong(DuFile::getSize).reversed());
        return unsortedChildren.subList(0, Math.min(limit, unsortedChildren.size()));
    }
}
