package ru.nsu.fit.mihanizzm.jdu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DuTreeBuilder {
    static private final int ZERO_DEPTH = 0;
    static private final int DEFAULT_SYMLINK_SIZE = 0;

    private static DuFile getNewDuFile(Path path, FileType type, int depth, boolean checkingLinks) {
        long size = 0;
        boolean unknownSize = false;
        try {
            size = getSizeOfFile(path);
        }
        catch (UnknownSizeOfFileException error) {
            unknownSize = true;
        }
        String name = getNameOfFile(path);

        if (type == FileType.REGULAR_FILE) {
            RegularFile regularFile = new RegularFile(path, size, name, depth, checkingLinks);
            regularFile.setHasUnknownSize(unknownSize);
            return regularFile;
        } else if (type == FileType.DIRECTORY) {
            Directory directory = new Directory(path, size, name, depth, checkingLinks);
            directory.setHasUnknownSize(unknownSize);
            return directory;
        } else if (type == FileType.SYMLINK) {
            SymLink symLink = new SymLink(path, size, name, depth, checkingLinks);
            Path realPath;
            try {
                realPath = symLink.getPath().toRealPath();
            }
            catch (IOException error) {
                throw new UnknownRealPathException("The symlink on path: \"" + symLink.getPath() + "\" has unknown real path, or the real path can't be accessed.");
            }
            symLink.setHasUnknownSize(unknownSize);
            symLink.setRealPath(realPath);
            return symLink;
        } else {
            throw new TreeBuilderFileTypeException("Chosen file type: \"" + type.toString() + "\" is invalid. Available file types are: REGULAR_FILE, DIRECTORY, SYMLINK.");
        }
    }

    private static String getNameOfFile(Path filePath) {
        return filePath.getFileName().toString();
    }

    private static long getSizeOfFile(Path filePath) {
        long size = 0;
        if (Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
            List<Path> folderFiles;
            try {
                folderFiles = Files.list(filePath).toList();
            }
            catch (IOException error) {
                throw new FileGettingListException("Can't get the list of files in directory on path: \"" + filePath + "\". List of files is can't be accessed or it's not a directory.");
            }
            for (Path file: folderFiles) {
                if (Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) {
                    size += getSizeOfFile(file);
                }
                else if (Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
                    try {
                        size += Files.size(file);
                    }
                    catch (IOException error) {
                        size += 0;
                    }
                }
                else if (Files.isSymbolicLink(file)) {
                    try {
                        size += Files.size(file);
                    }
                    catch (IOException error) {
                        size += 0;
                    }
                }
            }
        } else if (Files.isRegularFile(filePath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                size += Files.size(filePath);
            }
            catch (IOException error) {
                size += 0;
                throw new UnknownSizeOfFileException("The size of file on path: \"" + filePath + "\" is unknown or file can't be accessed.");
            }
        } else if (Files.isSymbolicLink(filePath)) {
            size = DEFAULT_SYMLINK_SIZE;
        } else {
            throw new UnknownFileTypeException("File on path \"" + filePath + "\" has unknown type or can't be accessed.");
        }
        return size;
    }

    public static List<DuFile> createChildren(Directory directory) {
        List<DuFile> children = new ArrayList<>();
        List<Path> childrenList;
        try {
            childrenList = Files.list(directory.getPath()).toList();
        }
        catch (IOException error) {
            throw new FileGettingListException("Can't get the list of files in directory on path: \"" + children + "\". List of files is can't be accessed or it's not a directory.");
        }

        for (Path child: childrenList) {
            if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                Directory childDirectory = (Directory) getNewDuFile(child, FileType.DIRECTORY, directory.getDepth() + 1, directory.getCheckingLinks());
                children.add(childDirectory);
            } else if (Files.isRegularFile(child, LinkOption.NOFOLLOW_LINKS)) {
                RegularFile childRegularFile = (RegularFile) getNewDuFile(child, FileType.REGULAR_FILE, directory.getDepth() + 1, directory.getCheckingLinks());
                children.add(childRegularFile);
            } else if (Files.isSymbolicLink(child)) {
                SymLink childSymLink = (SymLink) getNewDuFile(child, FileType.SYMLINK, directory.getDepth() + 1, directory.getCheckingLinks());
                children.add(childSymLink);
            } else {
                throw new UnknownFileTypeException("File on path \"" + child + "\" has unknown type or can't be accessed.");
            }

        }
        return children;
    }

    static public List<DuFile> getLimitedChildren(Directory dir, int limit) {
        List<DuFile> unsortedChildren = dir.getChildren();
        unsortedChildren.sort(Comparator.comparingLong(DuFile::getSize).reversed());
        return unsortedChildren.subList(0, Math.min(limit, unsortedChildren.size()));
    }

    public static DuFile resolveSymLink(SymLink symlink) {
        Path realPath = symlink.getRealPath();

        if (Files.isDirectory(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return getNewDuFile(realPath, FileType.DIRECTORY, symlink.getDepth(), symlink.getCheckingLinks());
        } else if (Files.isRegularFile(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return getNewDuFile(realPath, FileType.REGULAR_FILE, symlink.getDepth(), symlink.getCheckingLinks());
        } else if (Files.isSymbolicLink(realPath)) {
            SymLink nestedLink = (SymLink) getNewDuFile(realPath, FileType.SYMLINK, symlink.getDepth(), symlink.getCheckingLinks());
            return resolveSymLink(nestedLink);
        } else {
            throw new UnknownFileTypeException("File on path \"" + symlink.getRealPath().toString() + "\" has unknown type or can't be accessed.");
        }
    }

    public static DuFile buildFileTree(CommandLineOptions options) {
        DuFile root = null;

        if (Files.isDirectory(options.rootPath())) {
            root = getNewDuFile(options.rootPath(), FileType.DIRECTORY, ZERO_DEPTH, options.isCheckingSymLinks());
        } else if (Files.isRegularFile(options.rootPath())) {
            root = getNewDuFile(options.rootPath(), FileType.REGULAR_FILE, ZERO_DEPTH, options.isCheckingSymLinks());
        } else if (Files.isSymbolicLink(options.rootPath())) {
            root = getNewDuFile(options.rootPath(), FileType.SYMLINK, ZERO_DEPTH, options.isCheckingSymLinks());
        }

        return root;
    }

    enum FileType {
        REGULAR_FILE,
        DIRECTORY,
        SYMLINK
    }
}
