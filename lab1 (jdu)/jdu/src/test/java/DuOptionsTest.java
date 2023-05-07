import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.exception.CommandLineArgumentsException;
import ru.nsu.fit.mihanizzm.jdu.CommandLineOptions;
import ru.nsu.fit.mihanizzm.jdu.CommandLineParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DuOptionsTest extends DuTest {
    @Test
    public void testDefaultOptions() {
        String[] args = { };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 5, 3, false);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testCustomDepth() {
        String[] args = { "--depth", "10" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 5, 10, false);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testCustomLimit() {
        String[] args = { "--limit", "10" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 10, 3, false);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testCustomPath() throws IOException {
        Path customPath = Files.createTempFile("custom_file", null);
        String[] args = { customPath.toString() };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(customPath, 5, 3, false);

        Files.delete(customPath);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testCustomLinkChecking() {
        String[] args = { "-L" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 5, 3, true);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testAllCustomOptions() throws IOException {
        Path tempPath = Files.createTempFile("another_custom_file", null);
        String[] args = { "--limit", "1", "--depth", "1", "-L", tempPath.toString()};

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(tempPath, 1, 1, true);

        Files.delete(tempPath);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testLinksAndDepth() {
        String[] args = { "--depth", "1", "-L" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 5, 1, true);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testLinksAndLimit() {
        String[] args = { "--limit", "1", "-L" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 1, 3, true);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testLimitAndDepth() {
        String[] args = { "--limit", "1", "--depth", "1" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of(System.getProperty("user.dir")), 1, 1, false);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testPathWithSpaces() throws IOException {
        Path fileWithSpaces = Files.createTempFile("file with spaces in a name", null);
        String[] args = { "--limit", "1", "--depth", "1", fileWithSpaces.toString() };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(fileWithSpaces, 1, 1, false);

        Files.delete(fileWithSpaces);

        TestCase.assertEquals(expected, actual);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testInvalidPath() {
        String[] args = { "--limit", "1", "--depth", "1", "aboba" };

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testManyPaths() throws IOException {
        Path file1 = Files.createTempFile("temp1", null);
        Path file2 = Files.createTempFile("temp2", null);
        String[] args = { "--limit", "1", "--depth", "1", file1.toString(), file2.toString() };

        CommandLineParser.getCmdOptions(args);

        Files.delete(file1);
        Files.delete(file2);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testNegativeNumberOfArgumentValue() {
        String[] args = { "--limit", "-1", "--depth", "1" };

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testTooBigNumber() {
        String[] args = { "--limit", "23894798325348954931", "--depth", "1" };

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testUnknownOption() {
        String[] args = { "--ya_lomal_steklo", "1", "--depth", "1" };

        CommandLineParser.getCmdOptions(args);
    }

}
