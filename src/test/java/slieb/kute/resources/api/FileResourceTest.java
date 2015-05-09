package slieb.kute.resources.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.resources.Resources;
import slieb.kute.resources.implementations.FileResource;

import java.io.File;
import java.nio.file.Files;


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
        Resources.writeResource(fileResource, randomText);
        Assert.assertEquals(randomText, Resources.readResource(fileResource));

        Resources.writeResource(fileResource, randomText);
        Assert.assertEquals(randomText, Resources.readResource(fileResource));
    }

    @Test
    public void testEquals() throws Exception {
        Assert.assertEquals(fileResource, new FileResource("/example.txt", temporaryFile));
        Assert.assertNotEquals(fileResource, new FileResource("/example.jpg", temporaryFile));
    }

}