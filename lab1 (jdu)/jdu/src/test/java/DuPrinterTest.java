import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.CommandLineOptions;
import ru.nsu.fit.mihanizzm.jdu.DuPrinter;
import ru.nsu.fit.mihanizzm.jdu.model.RegularFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class DuPrinterTest extends DuTest {
    @Test
    public void testOneFile() throws IOException {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        DuPrinter testPrinter = new DuPrinter(opts);
        testPrinter.setPrintStream(System.out);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 0, "foo.txt");

        testPrinter.getPrintInfo(file);

        String actual = testPrinter.getPrintStream().toString();
        String expected = "foo.txt [0B]\n";

        TestCase.assertEquals(actual, expected);
    }
}
