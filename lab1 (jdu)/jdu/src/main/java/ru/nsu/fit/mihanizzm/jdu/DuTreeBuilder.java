package ru.nsu.fit.mihanizzm.jdu;

import ru.nsu.fit.mihanizzm.jdu.exception.TreeBuilderException;
import ru.nsu.fit.mihanizzm.jdu.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Builds file tree for a given path.
 * <p/>
 * Each tree node has info about file path, size, its type and its children.
 * <p/>
 * Tree nodes can be one of:
 * <ul>
 *     <li>regular file</li>
 *     <li>directory(with sub-files as tree children)</li>
 *     <li>symlink(real symlink path is a tree child of a symlink)</li>
 *     <li>unknown file(none of the above types, leaf node)</li>
 * </ul>
 * <p/>
 * Possible corner cases:
 * <ul>
 *     <li>cannot find file size - size is set to -1</li>
 *     <li>cannot traverse directory - children list is set to null</li>
 *     <li>cannot find real path for symlink - symlink real file is set to null</li>
 * </ul>
 * In all of this cases error is logged (but tree is still built).
 * <p/>
 * Cyclic symlinks are handled so that they point to a previously built node.
 * <br/>
 * E.g. for this case:
 * <pre>
 * foo
 *   slink -> foo
 * </pre>
 * Tree would look like:
 * <pre>
 * Directory foo (children = {slink})
 * Symlink slink (realDuFile = {foo})
 * </pre>
 * So in this case tree becomes a graph.
 */
public class DuTreeBuilder {
    private static final int UNKNOWN_FILE_SIZE = -1;

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
        long size;

        try {
            realPath = rootPath.toRealPath();
        }
        catch (IOException error) {
            realPath = null;
        }
        try {
            size = Files.getFileAttributeView(rootPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).readAttributes().size();
        }
        catch (IOException error) {
            size = UNKNOWN_FILE_SIZE;
        }

        DuFile realFile = resolveSymLink(realPath);

        return new SymLink(rootPath, size, realPath, realFile);
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
        try(Stream<Path> childrenStream = Files.list(dirPath)) {
            childrenList = childrenStream.toList();
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
        if (realPath == null) {
            return null;
        }

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

}
