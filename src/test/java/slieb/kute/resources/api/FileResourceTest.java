package slieb.kute.resources.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.resources.implementations.FileResource;

import java.io.File;
import java.nio.file.Files;

import static slieb.kute.Kute.readResource;
import static slieb.kute.Kute.writeResource;


public class FileResourceTest {

    private File temporaryFile;

    private FileResource fileResource;

    @Before
    public void setUp() throws Exception {
        temporaryFile = Files.createTempFile("temp", ".txt").toFile();
        temporaryFile.deleteOnExit();
        fileResource = new FileResource("/example.txt", temporaryFile);
    }

    @Test
    public void testGetNamespace() throws Exception {
        Assert.assertEquals("/example.txt", fileResource.getPath());
    }

    @Test
    public void testReaderAndWriter() throws Exception {
        String randomText = "Some random text";
        writeResource(fileResource, randomText);
        Assert.assertEquals(randomText, readResource(fileResource));

        writeResource(fileResource, randomText);
        Assert.assertEquals(randomText, readResource(fileResource));
    }

    @Test
    public void testEquals() throws Exception {
        Assert.assertEquals(fileResource, new FileResource("/example.txt", temporaryFile));
        Assert.assertNotEquals(fileResource, new FileResource("/example.jpg", temporaryFile));
    }

}