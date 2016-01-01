package slieb.kute.resources;


import org.apache.commons.io.IOUtils;
import org.junit.Test;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static slieb.kute.Kute.*;
import static slieb.kute.KuteIO.*;


public class ResourcesTest {

    @Test
    public void testReadResourceCorrectly() throws Exception {
        Resource.Readable readable = mock(Resource.Readable.class);
        when(readable.getReader()).thenAnswer(i -> new StringReader("content"));
        assertEquals("content", readResource(readable));
    }


    @Test
    public void testWriteResource() throws Exception {
        StringWriter writer = new StringWriter();
        Resource.Writable writable = mock(Resource.Writable.class);
        when(writable.getWriter()).thenReturn(writer);
        writeResource(writable, "content");
        assertEquals("content", writer.toString());
    }

    @Test
    public void testCopyResource() throws Exception {
        Resource.Readable readable = mock(Resource.Readable.class);
        when(readable.getReader()).thenAnswer(i -> new StringReader("content"));

        StringWriter writer = new StringWriter();
        Resource.Writable writable = mock(Resource.Writable.class);
        when(writable.getWriter()).thenReturn(writer);
        copyResource(readable, writable);
        assertEquals("content", writer.toString());
    }


    @Test
    public void testRename() throws Exception {
        Resource.Readable resource = mock(Resource.Readable.class);
        when(resource.getPath()).thenReturn("/path");
        Resource renamed = renameResource("/renamedPath", resource);
        assertEquals("/renamedPath", renamed.getPath());
    }

    @Test
    public void testInputStreamResource() throws Exception {
        try (InputStream inputStream = IOUtils.toInputStream("content")) {
            Resource.Readable readable = inputStreamResource("/path", () -> inputStream);
            assertEquals("content", readResource(readable));
            assertEquals("/path", readable.getPath());
        }
    }

    @Test
    public void testCacheResource() throws Exception {
        Resource.Readable readable = mock(Resource.Readable.class);
        CachedResource cached = cacheResource(readable);

        when(readable.getReader()).thenAnswer(invocationOnMock -> new StringReader("cache"));
        assertEquals("cache", readResource(cached));

        IntStream.range(0, 10000)
                .parallel()
                .forEach(i -> {
                    try {
                        when(readable.getReader()).thenAnswer(invocationOnMock -> new StringReader("clear"));
                        assertEquals("cache", readResource(cached));
                    } catch (IOException io) {
                        throw new RuntimeException(io);
                    }
                });

        cached.clear();
        assertEquals("clear", readResource(cached));

    }

    @Test
    public void testStringResource() throws Exception {
        final Resource.Readable readable = stringResource("/path", "content");
        IntStream.range(0, 10000)
                .parallel()
                .forEach(i -> {
                    try {
                        assertEquals("content", readResource(readable));
                        assertEquals("/path", readable.getPath());
                    } catch (IOException ignored) {
                        throw new RuntimeException(ignored);
                    }
                });
    }

}