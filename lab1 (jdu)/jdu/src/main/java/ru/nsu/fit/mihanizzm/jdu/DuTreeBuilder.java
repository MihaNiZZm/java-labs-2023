package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.Directory;
import ru.nsu.fit.mihanizzm.jdu.model.DuFile;
import ru.nsu.fit.mihanizzm.jdu.model.RegularFile;
import ru.nsu.fit.mihanizzm.jdu.model.SymLink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DuTreeBuilder {
    static private final int ZERO_DEPTH = 0;
    static private final int DEFAULT_SYMLINK_SIZE = 0;

    public static DuFile buildFileTree(CommandLineOptions options) {
        DuFile root = null;

        Path rootPath = options.rootPath();
        boolean checkingSymLinks = options.isCheckingSymLinks();
        if (Files.isDirectory(rootPath)) {
            root = getNewDuFile(rootPath, FileType.DIRECTORY, ZERO_DEPTH, checkingSymLinks);
        } else if (Files.isRegularFile(rootPath)) {
            // CR: separate method for each file type, remove FileType parameter
            root = getNewDuFile(rootPath, FileType.REGULAR_FILE, ZERO_DEPTH, checkingSymLinks);
        } else if (Files.isSymbolicLink(rootPath)) {
            root = getNewDuFile(rootPath, FileType.SYMLINK, ZERO_DEPTH, checkingSymLinks);
        }

        return root;
    }

    private static DuFile getNewDuFile(Path path, FileType type, int depth, boolean checkingLinks) {
        long size = 0;
        boolean unknownSize = false;
        try {
            // CR: calculate size after building children nodes
            size = getSizeOfFile(path);
        }
        catch (UnknownSizeOfFileException error) {
            unknownSize = true;
        }

        String name = getNameOfFile(path);

        if (type == FileType.REGULAR_FILE) {
            RegularFile regularFile = new RegularFile(path, size, name, depth, checkingLinks);
            // CR: set -1 to size
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
                // CR: move before ctor, pass result to ctor
                realPath = symLink.getPath().toRealPath();
            }
            catch (IOException error) {
                // CR: log error
                // CR: build symlink with unknown target
                throw new UnknownRealPathException("The symlink on path: \"" + symLink.getPath() + "\" has unknown real path, or the real path can't be accessed.");
            }
            symLink.setHasUnknownSize(unknownSize);
            symLink.setRealPath(realPath);
            return symLink;
        } else {
            // CR: log error
            // CR: add UnknownFile { name, size }
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
                // CR: add test
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

    // CR: build whole tree in DuTreeBuilder
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

    // CR: remove
    enum FileType {
        REGULAR_FILE,
        DIRECTORY,
        SYMLINK
    }
}
