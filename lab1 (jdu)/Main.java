import java.io.IOError;
import java.io.IOException;

import du.DiskUsage;

public class Main {
    public static void main(String[] args) throws IOException {
        DiskUsage.calculateDiskUsage(args);
    }
}