import ru.nsu.fit.mihanizzm.jdu.DuTreeBuilder;
import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.model.DuFile;
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
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", DuTreeElement.file("bar.txt")));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testManyFilesInDirectory() throws IOException {
        FileSystem fs = fileSystem();

        Path fooPath = fs.getPath("foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar.txt");
        Path fizPath = fooPath.resolve("fiz.txt");
        Path bazPath = fooPath.resolve("baz.txt");
        Path bozPath = fooPath.resolve("boz.txt");
        Path bezPath = fooPath.resolve("bez.txt");

        Files.createFile(barPath);
        Files.createFile(fizPath);
        Files.createFile(bazPath);
        Files.createFile(bozPath);
        Files.createFile(bezPath);

        CommandLineOptions opts = new CommandLineOptions(fooPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo",
                DuTreeElement.file("bar.txt"),
                DuTreeElement.file("fiz.txt"),
                DuTreeElement.file("baz.txt"),
                DuTreeElement.file("boz.txt"),
                DuTreeElement.file("bez.txt")));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testDirectoryInDirectoryInDirectory() throws IOException {
        FileSystem fs = fileSystem();

        Path fooPath = fs.getPath("foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar");
        Files.createDirectory(barPath);
        Path bazPath = barPath.resolve("baz");
        Files.createDirectory(bazPath);

        CommandLineOptions opts = new CommandLineOptions(fooPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", DuTreeElement.dir("bar", DuTreeElement.dir("baz"))));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testSymlinkInDirectory() throws IOException {
        FileSystem fs = fileSystem();

        Path fooPath = fs.getPath("foo");
        Files.createDirectory(fooPath);

        Path linkedFilePath = fooPath.resolve("file");
        Files.createFile(linkedFilePath);

        Path linkPath = fooPath.resolve("link");
        Files.createLink(linkPath, linkedFilePath);

        CommandLineOptions opts = new CommandLineOptions(fooPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", DuTreeElement.file("link")));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testOnlyRegularFile() throws IOException {
        FileSystem fs = fileSystem();

        Path fooPath = fs.getPath("foo");
        Files.createFile(fooPath);

        CommandLineOptions opts = new CommandLineOptions(fooPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.file("foo"));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyDirectory() throws IOException {
        FileSystem fs = fileSystem();

        Path dirPath = fs.getPath("dir");
        Files.createDirectory(dirPath);

        CommandLineOptions opts = new CommandLineOptions(dirPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.file("dir"));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testSymLink() throws IOException {
        FileSystem fs = fileSystem();


        Path linkedFilePath = fs.getPath("file");
        Files.createFile(linkedFilePath);

        Path linkPath = fs.getPath("link");
        Files.createLink(linkPath, linkedFilePath);

        CommandLineOptions opts = new CommandLineOptions(linkPath, 3, 5, false);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.file("link"));

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testRecursiveSymlink() throws IOException {
        FileSystem fs = fileSystem();

        Path link1Path = fs.getPath("link1");
        Files.createSymbolicLink(link1Path, link1Path);

        Path link2Path = fs.getPath("link2");
        Files.createSymbolicLink(link2Path, link1Path);

        CommandLineOptions opts = new CommandLineOptions(link2Path, 3, 5, true);

        DuFile actual = DuTreeBuilder.buildFileTree(opts);
        DuFile expected = DuTreeElement.tree(fs, DuTreeElement.file("link2"));

        TestCase.assertEquals(expected, actual);
    }
}
