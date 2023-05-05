package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// CR: javadoc
/*
* This class does common operations with {@link DuFile} objects such as:
        * <ul>
 *
         *   <li>building a tree of files: {@link #buildFileTree(CommandLineOptions)}</li>
        *
        *   <li>creating new instances of all DuFile inheritors:
        *     <ul>
 *       <li>{@link #getNewRegularFile(Path)}</li>
        *       <li>{@link #getNewDirectory(Path)}</li>
        *       <li>{@link #getNewSymLink(Path)}</li>
        *       <li>{@link #getNewUnknownFile(Path)}</li>
        *     </ul>
        *   </li>
        *
        *   <li>sorting the list of directory's contents by size in descending order: {@link #getLimitedNumberOfChildrenFiles(Directory, int)}
        *   where int parameter is number of content files you want to get. This method is used for printing a certain number of the heaviest files in a directory.</li>
        *
        *   <li>creating DuFile instances for contents of a directory: {@link #createChildren(Path)}.
        *   This method throws a {@link TreeBuilderException} in case when contents of a direcotry can't be accessed.
        *   Although it will not crash the program because this method used only while creating a new instance of a {@link Directory}
        *   in {@link #getNewDirectory(Path)} method where this type of exception is being catched.
        *   In case if the exception was thrown, the new {@link Directory} is created with children == null.
        *   </li>
        *
        *   <li>resolving the real path of a symbolic link and turns it into a {@link DuFile} instance: {@link #resolveSymLink(Path)}
        *   where Path parameter is the real path of a chosen symbolic link.</li>
        * </ul>
        * The result of building a file tree is being used in {@link DuPrinter} to print detailed file tree based on given
        * options and the file tree built by {@link DuTreeBuilder}.
        * <br><br>
 * There are some constants in {@link DuTreeBuilder} such as:
        * <ul>
 *   <li>{@link #DEFAULT_SYMLINK_SIZE}</li>
        *   <li>{@link #UNKNOWN_FILE_SIZE}</li>
        * </ul>
        * that represent default sizes of a {@link SymLink} and an {@link UnknownFile} resprectively.
        * <br><br>
 * There are no calculation for size of a symlink because there are no correct way to find a correct actual size of it (or at least I couldn't find it).
         * <br><br>
 * Sizes of files with unknown size (-1) won't be counted in the size of {@link Directory} they belong to. Size of this files will be printed as "unknown size"
         * in {@link DuPrinter}.

*/

/**
 * This class does support common operations with {@link DuFile} instances.
 * The main goal of this class is to build a file tree accorging to given {@link CommandLineOptions}.
 * There are some corner cases and important details in the proccess of building a tree that should be meant:
 * <ul>
 *   <li>A file can't be accessed. In that case a new instance of a {@link UnknownFile} is being created and added into a file tree.</li>
 *   <li>Contents of a directory can't be accessed. In that case a new instance of a {@link Directory} is being created with no contents and with a size of 0 bytes.</li>
 *   <li>A file can be accessed but it size can't. In that case a new instance of a {@link RegularFile} is being created with so called "unknown size" that equals to -1.
 *   The size of this file will not be counted in the size of {@link Directory} it belongs to.</li>
 *   <li>The real path of a symbolic link can't be accessed. In that case a new instance of a {@link SymLink} is being created.
 *   In case you try to resolve that link you will get an instance of an {@link UnknownFile}.</li>
 *   <li>Recursive symbolic links. Since {@link DuTreeBuilder} builds a file tree without resolving real paths of symbolic links,
 *   it will not contain any information about loops between them. Therefore, a structure of a built file tree is a tree (a connected acyclic graph).
 *   All loops are being resolved during the printing stage.</li>
 *   <li>A built file tree contains all end files (regular files) that belongs to chosen root path.
 *   Example: you have a directory dir1 that contains directories dir11 and dir12 and also contains a regular file bar.txt.
 *   Also dir11 contains a regular file foo.txt and dir12 contains a regular file baz.txt.
 *   The result of building a tree will be a file tree that contains dir1 as a root, dir11, foo.txt, dir12, baz.txt and bar.txt.
 *   That means that all files was covered and there are no files that are not counted.
 *   </li>
 * </ul>
 * The constant {@link #DEFAULT_SYMLINK_SIZE} is being used to set a size of a symbolic link.
 * The constant {@link #UNKNOWN_FILE_SIZE} is being used to set a size of files that can't be accessed or files with unknown or inaccesible size.
 */
public class DuTreeBuilder {
    private static final int UNKNOWN_FILE_SIZE = -1;
    // CR: find out how to find size
    // Ne poluchaestya
    private static final int DEFAULT_SYMLINK_SIZE = 0;

    private static Directory getNewDirectory(Path rootPath) {
        long size = 0;

        List<DuFile> childrenFiles;
        try {
            childrenFiles = createChildren(rootPath);
        }
        catch (TreeBuilderException error) {
            return new Directory(rootPath, 0, null);
        }

        for (DuFile childFile : childrenFiles) {
            if (!childFile.hasUnknownSize()) {
                size += childFile.getSize();
            }
        }

        return new Directory(rootPath, size, childrenFiles);
    }

    private static RegularFile getNewRegularFile(Path rootPath) {
        long size;

        try {
            size = Files.size(rootPath);
        }
        catch (IOException error) {
            size = UNKNOWN_FILE_SIZE;
        }

        return new RegularFile(rootPath, size);
    }

    private static SymLink getNewSymLink(Path rootPath) {
        Path realPath;
        try {
            realPath = rootPath.toRealPath();
        }
        catch (IOException error) {
            realPath = null;
        }

        DuFile realFile = resolveSymLink(realPath);

        return new SymLink(rootPath, DEFAULT_SYMLINK_SIZE, realPath, realFile);
    }

    private static UnknownFile getNewUnknownFile(Path rootPath) {
        return new UnknownFile(rootPath, UNKNOWN_FILE_SIZE);
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

    private static List<DuFile> createChildren(Path dirPath) {
        List<DuFile> children = new ArrayList<>();
        List<Path> childrenList;
        try {
            // CR: close list
            childrenList = Files.list(dirPath).toList();
            Files.list(dirPath).close();
        }
        catch (IOException error) {
            throw new TreeBuilderException("Can't get the list of files in directory on path: \"" + dirPath + "\". List of files is can't be accessed or it's not a directory.");
        }

        for (Path child: childrenList) {
            if (Files.isSymbolicLink(child)) {
                SymLink childSymLink = getNewSymLink(child);
                children.add(childSymLink);
            } else if (Files.isDirectory(child)) {
                Directory childDirectory = getNewDirectory(child);
                children.add(childDirectory);
            } else if (Files.isRegularFile(child)) {
                RegularFile childRegularFile = getNewRegularFile(child);
                children.add(childRegularFile);
            } else {
                UnknownFile unknownFile = getNewUnknownFile(child);
                children.add(unknownFile);
            }

        }
        return children;
    }

    public static DuFile resolveSymLink(Path realPath) {
        if (Files.isDirectory(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return getNewDirectory(realPath);
        } else if (Files.isRegularFile(realPath, LinkOption.NOFOLLOW_LINKS)) {
            return getNewRegularFile(realPath);
        } else if (Files.isSymbolicLink(realPath)) {
            SymLink nestedLink = getNewSymLink(realPath);
            return resolveSymLink(nestedLink.getRealPath());
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
