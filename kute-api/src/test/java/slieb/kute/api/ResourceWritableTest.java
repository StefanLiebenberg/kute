package slieb.kute.api;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicReference;

import static slieb.kute.api.TestUtils.EXPECTED_CONTENT;
import static slieb.kute.api.TestUtils.EXPECTED_PATH;

public class ResourceWritableTest {

    final AtomicReference<StringWriter> reference = new AtomicReference<>();
    Resource.Writable writable;

    @Before
    public void setup() {
        writable = TestUtils.writableResource(EXPECTED_PATH, reference);
    }

    @Test
    public void testResourceWritableGetDefaultWriter() throws IOException {

        try (Writer writer = writable.getWriter()) {
            Assert.assertNotNull(writer);
            IOUtils.write(EXPECTED_CONTENT, writer);
        }
        Assert.assertEquals(EXPECTED_CONTENT, reference.get().toString());
        Assert.assertEquals(EXPECTED_PATH, writable.getPath());
    }

    @Test
    public void testResourceWritableUseWriter() throws IOException {
        writable.useWriter(writer -> {
            Assert.assertNotNull(writer);
            IOUtils.write(EXPECTED_CONTENT, writer);
        });
        Assert.assertEquals(EXPECTED_CONTENT, reference.get().toString());
        Assert.assertEquals(EXPECTED_PATH, writable.getPath());
    }

    @Test(expected = IOException.class)
    public void testResourceWritableUseWriterThrowsIO() throws IOException {
        writable.useWriter(writer -> {
            throw new IOException();
        });
    }

    @Test
    public void testResourceWritableUseOutputStream() throws IOException {
        writable.useOutputStream(outputStream -> {
            Assert.assertNotNull(outputStream);
            IOUtils.write(EXPECTED_CONTENT, outputStream);
        });
        Assert.assertEquals(EXPECTED_CONTENT, reference.get().toString());
        Assert.assertEquals(EXPECTED_PATH, writable.getPath());
    }

    @Test(expected = IOException.class)
    public void testResourceWritableUseOutputStreamThrowsIO() throws IOException {
        writable.useOutputStream(outputStream -> {
            throw new IOException();
        });
    }
}
