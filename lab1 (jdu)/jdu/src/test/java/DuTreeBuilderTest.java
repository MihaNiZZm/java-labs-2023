import ru.nsu.fit.mihanizzm.jdu.DuTreeBuilder;
import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.DuFile;
import ru.nsu.fit.mihanizzm.jdu.CommandLineOptions;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class DuTreeBuilderTest extends DuTest {
    @Test
    public void testOneFileInDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar.txt");
        Files.createFile(barPath);

        CommandLineOptions opts = new CommandLineOptions(fooPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", DuTreeElement.file("bar.txt")), 5, false);
        TestCase.assertEquals(expected, actual);
    }
}
