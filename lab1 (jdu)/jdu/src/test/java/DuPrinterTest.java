import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.CommandLineOptions;
import ru.nsu.fit.mihanizzm.jdu.DuPrinter;
import ru.nsu.fit.mihanizzm.jdu.model.RegularFile;

import java.io.*;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;

public class DuPrinterTest extends DuTest {

    /*

    CR:
    - regular file
    - symlink
    - directory with regular file
    - empty directory
    - directory with symlink
    - directory with another dir
    - recursive symlink
    - -1 size
    - unknown file

     */

    @Test
    public void testOneFile() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        DuPrinter printer = new DuPrinter(opts, stream);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 0, "foo.txt");
        printer.getPrintInfo(file);

        assertEquals("foo.txt [0B]\n", baos.toString());
    }
}
