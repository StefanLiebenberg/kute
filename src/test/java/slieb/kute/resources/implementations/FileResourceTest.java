package slieb.kute.resources.implementations;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.resources.FileResource;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;
import static slieb.kute.utils.KuteIO.readResource;
import static slieb.kute.utils.KuteIO.writeResource;


public class FileResourceTest {

    private File temporaryFile;

    private FileResource fileResource;

    @Before
    public void setUp() throws Exception {
        temporaryFile = Files.createTempFile("temp", ".txt").toFile();
        temporaryFile.deleteOnExit();
        fileResource = new FileResource(temporaryFile);
    }

    @Test
    public void testGetNamespace() throws Exception {
        assertEquals(temporaryFile.getPath(), fileResource.getPath());
    }

    @Test
    public void testReaderAndWriter() throws Exception {
        String randomText = "Some random text";
        writeResource(fileResource, randomText);
        assertEquals(randomText, readResource(fileResource));

        writeResource(fileResource, randomText);
        assertEquals(randomText, readResource(fileResource));
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(fileResource, new FileResource(temporaryFile));
        assertNotEquals(fileResource, new FileResource("/example.jpg", temporaryFile));
    }

    @Test
    public void testGetInputStream() throws IOException {
        List<String> lines = Lists.newArrayList("input stream test value");
        try (OutputStream outputStream = new FileOutputStream(fileResource.getFile())) {
            IOUtils.writeLines(lines, System.lineSeparator(), outputStream);
        }

        try (InputStream inputStream = fileResource.getInputStream()) {
            assertNotNull(inputStream);
            assertEquals(lines, IOUtils.readLines(inputStream));
        }
    }

    @Test
    public void testGetOutputStream() throws IOException {
        List<String> lines = Lists.newArrayList("output stream test value");
        try (OutputStream outputStream = fileResource.getOutputStream()) {
            assertNotNull(outputStream);
            IOUtils.writeLines(lines, System.lineSeparator(), outputStream);
        }

        assertNotNull(fileResource.getFile());
        assertTrue(fileResource.getFile().exists());
        try (InputStream inputStream = new FileInputStream(fileResource.getFile())) {
            assertEquals(lines, IOUtils.readLines(inputStream));
        }
    }


}