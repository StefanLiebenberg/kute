package org.slieb.kute.resources.implementations;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slieb.kute.Kute;
import org.slieb.kute.resources.FileResource;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;
import static org.slieb.kute.KuteIO.readResource;
import static org.slieb.kute.KuteIO.writeResource;

public class FileResourceTest {

    private File temporaryFile;

    private FileResource fileResource;

    @Before
    public void setUp() throws Exception {
        temporaryFile = Files.createTempFile("temp", ".txt").toFile();
        temporaryFile.deleteOnExit();
        fileResource = Kute.fileResource(temporaryFile);
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
        assertEquals(fileResource, Kute.fileResource(temporaryFile));
        assertNotEquals(fileResource, Kute.fileResource("/example.jpg", temporaryFile));
    }

    @Test
    public void testGetInputStream() throws IOException {
        List<String> lines = Lists.newArrayList("input stream test value");
        try (OutputStream outputStream = new FileOutputStream(fileResource.getFile())) {
            IOUtils.writeLines(lines, System.lineSeparator(), outputStream);
        }

        fileResource.useInputStream(inputStream -> {
            assertNotNull(inputStream);
            assertEquals(lines, IOUtils.readLines(inputStream));
        });
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