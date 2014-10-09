package slieb.kute.utilities.fs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;

public class DirectoryIterableTest {

    File directory;

    @Before
    public void setUp() throws Exception {
        directory = Files.createTempDirectory(getClass().getName()).toFile();
        directory.deleteOnExit();

        createFile("/path/to/file/one.txt", "file one");
        createFile("/path/to/other/two.txt", "file two");
        createFile("/smoker.txt", "smoker");
    }

    private void createFile(String path, String content) throws IOException {
        File file = new File(directory, path);

        File parent = file.getParentFile();
        parent.mkdirs();
        while (parent != null && !parent.equals(directory)) {
            parent.deleteOnExit();
            parent = parent.getParentFile();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    private void catchFiles(Iterable<File> iterable, Collection<File> collection) {
        for (File file : iterable) {
            collection.add(file);
        }
    }

    @Test
    public void testIterator() throws Exception {
        DirectoryIterable fileiterable = new DirectoryIterable(directory);
        Collection<File> caught = new HashSet<>();
        catchFiles(fileiterable, caught);

        Assert.assertEquals(3, caught.size());
        Assert.assertTrue(caught.contains(new File(directory, "/path/to/file/one.txt")));
        Assert.assertTrue(caught.contains(new File(directory, "/smoker.txt")));
        Assert.assertTrue(caught.contains(new File(directory, "/path/to/other/two.txt")));
    }

    @Test
    public void testCanCatchDirectories() throws Exception {
        DirectoryIterable fileiterable = new DirectoryIterable(directory, true, true);
        Collection<File> caught = new HashSet<>();
        catchFiles(fileiterable, caught);
        Assert.assertEquals(7, caught.size());
    }

    
}