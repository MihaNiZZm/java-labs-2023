import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.CommandLineArgumentsException;
import ru.nsu.fit.mihanizzm.jdu.CommandLineOptions;
import ru.nsu.fit.mihanizzm.jdu.CommandLineParser;

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
    public void testCustomPath() {
        String[] args = { "D:/Photos" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of("D:/Photos"), 5, 3, false);

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
    public void testAllCustomOptions() {
        String[] args = { "--limit", "1", "--depth", "1", "-L", "D:/Photos" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of("D:/Photos"), 1, 1, true);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testLinksAndDepth() {
        String[] args = { "--depth", "1", "-L", "D:/Photos" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of("D:/Photos"), 5, 1, true);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testLinksAndLimit() {
        String[] args = { "--limit", "1", "-L", "D:/Photos" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of("D:/Photos"), 1, 3, true);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testLimitAndDepth() {
        String[] args = { "--limit", "1", "--depth", "1", "D:/Photos" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of("D:/Photos"), 1, 1, false);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testPathWithSpaces() {
        String[] args = { "--limit", "1", "--depth", "1", "D:/Photos/After editing" };

        CommandLineOptions actual = CommandLineParser.getCmdOptions(args);
        CommandLineOptions expected = new CommandLineOptions(Path.of("D:/Photos/After editing"), 1, 1, false);

        TestCase.assertEquals(expected, actual);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testInvalidPath() {
        String[] args = { "--limit", "1", "--depth", "1", "aboba" };

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testManyPaths() {
        String[] args = { "--limit", "1", "--depth", "1", "D:/Photos", "C:/Windows" };

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testNegativeNumberOfArgumentValue() {
        String[] args = { "--limit", "-1", "--depth", "1", "D:/Photos"};

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testTooBigNumber() {
        String[] args = { "--limit", "23894798325348954931", "--depth", "1", "D:/Photos" };

        CommandLineParser.getCmdOptions(args);
    }

    @Test(expected = CommandLineArgumentsException.class)
    public void testUnknownOption() {
        String[] args = { "--ya_lomal_steklo", "1", "--depth", "1", "D:/Photos" };

        CommandLineParser.getCmdOptions(args);
    }

}
