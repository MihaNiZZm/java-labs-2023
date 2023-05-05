import org.junit.Test;
import ru.nsu.fit.mihanizzm.jdu.CommandLineOptions;
import ru.nsu.fit.mihanizzm.jdu.DuPrinter;
import ru.nsu.fit.mihanizzm.jdu.model.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class DuPrinterTest extends DuTest {

    @Test
    public void testOneFile() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 0);

        DuPrinter.print(file, opts, stream);

        assertEquals("foo.txt [0B]\n", baos.toString());
    }

    @Test
    public void testFollowedSymLink() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 0);
        SymLink symLink = new SymLink(Path.of("link"), 0, file.getPath(), file);

        DuPrinter.print(symLink, opts, stream);

        assertEquals("Followed symlink: foo.txt [0B]\n", baos.toString());
    }

    @Test
    public void testUnFollowedSymLink() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 0);
        SymLink symLink = new SymLink(Path.of("link"), 0, file.getPath(), file);

        DuPrinter.print(symLink, opts, stream);

        assertEquals("Unfollowed symlink: link [0B]\n", baos.toString());
    }

    @Test
    public void testDirectoryWithRegularFile() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 5);
        List<DuFile> childrenList = new ArrayList<>();
        childrenList.add(file);
        Directory dir = new Directory(Path.of("directory"), 5, childrenList);

        DuPrinter.print(dir, opts, stream);

        assertEquals("directory [5B]\n\tfoo.txt [5B]\n", baos.toString());
    }

    @Test
    public void testEmptyDirectory() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        List<DuFile> childrenList = new ArrayList<>();
        Directory dir = new Directory(Path.of("directory"), 0, childrenList);

        DuPrinter.print(dir, opts, stream);

        assertEquals("directory [0B]\n", baos.toString());
    }

    @Test
    public void testDirectoryWithFollowedSymlink() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 5);
        SymLink link = new SymLink(Path.of("link"), 0, file.getPath(), file);

        List<DuFile> childrenList = new ArrayList<>();
        childrenList.add(link);
        Directory dir = new Directory(Path.of("directory"), 0, childrenList);

        DuPrinter.print(dir, opts, stream);

        assertEquals("directory [0B]\n\tFollowed symlink: foo.txt [5B]\n", baos.toString());
    }

    @Test
    public void testDirectoryWithUnfollowedSymlink() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 5);
        SymLink link = new SymLink(Path.of("link"), 0, file.getPath(), file);

        List<DuFile> childrenList = new ArrayList<>();
        childrenList.add(link);
        Directory dir = new Directory(Path.of("directory"), 0, childrenList);

        DuPrinter.print(dir, opts, stream);

        assertEquals("directory [0B]\n\tUnfollowed symlink: link [0B]\n", baos.toString());
    }

    @Test
    public void testDirectoryWithAnotherDirectory() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile file = new RegularFile(Path.of("foo.txt"), 5);

        List<DuFile> childrenList1 = new ArrayList<>();
        childrenList1.add(file);
        Directory dir1 = new Directory(Path.of("directory1"), 5, childrenList1);

        List<DuFile> childrenList2 = new ArrayList<>();
        childrenList2.add(dir1);
        Directory dir2 = new Directory(Path.of("directory2"), 5, childrenList2);


        DuPrinter.print(dir2, opts, stream);

        assertEquals("directory2 [5B]\n\tdirectory1 [5B]\n\t\tfoo.txt [5B]\n", baos.toString());
    }

    @Test
    public void testRecursiveSymlink() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        SymLink link = new SymLink(Path.of("link"), 0, null, null);

        List<DuFile> childrenList = new ArrayList<>();
        childrenList.add(link);
        Directory dir = new Directory(Path.of("directory"), 0, childrenList);

        link.setRealPath(dir.getPath());
        link.setRealFile(dir);

        DuPrinter.print(dir, opts, stream);

        assertEquals("directory [0B]\n\tFollowed symlink: directory [0B]\n", baos.toString());
    }

    @Test
    public void testRepeatedDirectoriesWithSymlink() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        SymLink link = new SymLink(Path.of("link"), 0, null, null);

        List<DuFile> childrenList2 = new ArrayList<>();
        childrenList2.add(new RegularFile(Path.of("bar.txt"), 3));
        Directory dirBar = new Directory(Path.of("dirBar"), 3, childrenList2);

        List<DuFile> childrenList1 = new ArrayList<>();
        childrenList1.add(link);
        childrenList1.add(dirBar);
        Directory dirFoo = new Directory(Path.of("dirFoo"), 3, childrenList1);

        link.setRealPath(dirBar.getPath());
        link.setRealFile(dirBar);

        DuPrinter.print(dirFoo, opts, stream);

        assertEquals("dirFoo [3B]\n\tdirBar [3B]\n\t\tbar.txt [3B]\n\tFollowed symlink: dirBar [3B]\n\t\tbar.txt [3B]\n", baos.toString());
    }

    @Test
    public void testRegularFileWithMinusOneSize() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        RegularFile fileWithUnknownSize = new RegularFile(Path.of("failik^_^"), -1);

        DuPrinter.print(fileWithUnknownSize, opts, stream);

        assertEquals("failik^_^ [unknown] (file has unknown size)\n", baos.toString());
    }

    @Test
    public void testUnknownFile() {
        CommandLineOptions opts = new CommandLineOptions(null, 5, 3, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);

        UnknownFile unknownFile = new UnknownFile(Path.of("badFile"), -1);

        DuPrinter.print(unknownFile, opts, stream);

        assertEquals("badFile [unknown] (file is unknown)\n", baos.toString());
    }
}
