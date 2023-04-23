import ru.nsu.fit.mihanizzm.jdu.model.Directory;
import ru.nsu.fit.mihanizzm.jdu.model.DuFile;
import ru.nsu.fit.mihanizzm.jdu.model.RegularFile;
import ru.nsu.fit.mihanizzm.jdu.model.SymLink;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;

public record DuTreeElement(Type type, String path, List<DuTreeElement> children) {

    public static DuFile tree(FileSystem fs, DuTreeElement root) {
        return buildTree(root, fs.getPath(""));
    }

    private static DuFile buildTree(DuTreeElement treeElement, Path parentPath) {
        Path currentPath = parentPath.resolve(treeElement.path);
        long size;
        String name = currentPath.getFileName().toString();

        if (treeElement.type == Type.FILE) {
            try {
                size = Files.size(currentPath);
            }
            catch (IOException error) {
                size = 0;
            }
            return new RegularFile(currentPath, size, name);
        } else if (treeElement.type == Type.SYM) {
            size = 0;
            Path realPath;
            try {
                realPath = currentPath.toRealPath();
            }
            catch (IOException error) {
                realPath = null;
            }
            return new SymLink(currentPath, size, name, realPath);
        } else if (treeElement.type == Type.DIR) {
            size = treeElement.getDirSize(currentPath);
            List<DuFile> children = treeElement.children.stream().map(c -> buildTree(c, currentPath)).toList();
            return new Directory(currentPath, size, name, children);
        }
        else {
            throw new RuntimeException("Unknown file type.");
        }
    }

    private long getDirSize(Path currentPath) {
        long size = 0;
        List<Path> folderFiles;

        try {
            folderFiles = Files.list(currentPath).toList();
        }
        catch (IOException error) {
            folderFiles = null;
        }
        assert folderFiles != null;

        for (Path file: folderFiles) {
            if (Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) {
                size += getDirSize(file);
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
        return size;
    }

    public static DuTreeElement dir(String name, DuTreeElement... files) {
        return new DuTreeElement(Type.DIR, name, List.of(files));
    }

    public static DuTreeElement file(String name) {
        return new DuTreeElement(Type.FILE, name, null);
    }

    private enum Type {
        DIR,
        FILE,
        SYM
    }
}
