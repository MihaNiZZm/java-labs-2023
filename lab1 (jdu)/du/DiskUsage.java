package du;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.List;

public class DiskUsage {
    public static void calculateDiskUsage(String[] args) throws IOException {
        getSizeOfFile(Paths.get(""));
    }

    

    private static long getSizeOfFile(Path rootFile) throws IOException {
        long size = 0;
        if (Files.isDirectory(rootFile)) {
            Stream<Path> folderFiles = Files.list(rootFile);

            if (folderFiles == null) {
                return size;
            }

            List<Path> directories = folderFiles.filter(filePath -> Files.isDirectory(filePath)).toList();
            for (Path directory: directories) {
                size += getSizeOfFile(directory);
            }

            List<Path> files = folderFiles.filter(filePath -> !(Files.isDirectory(filePath))).toList();
            for (Path file: files) {
                size += Files.size(file);
            }

            folderFiles.close();
        }
        return size;
    }
}
