package slieb.kute.resources;


import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.resources.implementations.CachedResource;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static slieb.kute.Kute.*;


public class ResourcesTest {

    @Test
    public void testReadResourceCorrectly() throws Exception {
        Resource.Readable readable = mock(Resource.Readable.class);
        when(readable.getReader()).thenAnswer(i -> new StringReader("content"));
        assertEquals("content", Kute.readResource(readable));
    }

    @Test(expected = RuntimeException.class)
    public void testReadResourceUnsafe() {
        readResourceUnsafe(new Resource.Readable() {
            @Override
            public String getPath() {
                return "/path";
            }

            @Override
            public Reader getReader() throws IOException {
                throw new IOException("expected io.");
            }
        });
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
        Resource resource = mock(Resource.class);
        when(resource.getPath()).thenReturn("/path");
        Resource renamed = Kute.renameResource("/renamedPath", resource);
        assertEquals("/renamedPath", renamed.getPath());
    }

    @Test
    public void testInputStreamResource() throws Exception {
        try (InputStream inputStream = IOUtils.toInputStream("content")) {
            Resource.Readable readable = Kute.inputStreamResource("/path", () -> inputStream);
            assertEquals("content", Kute.readResource(readable));
            assertEquals("/path", readable.getPath());
        }
    }

    @Test
    public void testCacheResource() throws Exception {
        Resource.Readable readable = mock(Resource.Readable.class);
        CachedResource cached = Kute.cacheResource(readable);

        when(readable.getReader()).thenAnswer(invocationOnMock -> new StringReader("cache"));
        assertEquals("cache", Kute.readResource(cached));

        IntStream.range(0, 10000)
                .parallel()
                .forEach(i -> {
                    try {
                        when(readable.getReader()).thenAnswer(invocationOnMock -> new StringReader("clear"));
                        assertEquals("cache", Kute.readResource(cached));
                    } catch (IOException io) {
                        throw new RuntimeException(io);
                    }
                });

        cached.clear();
        assertEquals("clear", Kute.readResource(cached));

    }

    @Test
    public void testStringResource() throws Exception {
        final Resource.Readable readable = Kute.stringResource("/path", "content");
        IntStream.range(0, 10000)
                .parallel()
                .forEach(i -> {
                    try {
                        assertEquals("content", Kute.readResource(readable));
                        assertEquals("/path", readable.getPath());
                    } catch (IOException ignored) {
                        throw new RuntimeException(ignored);
                    }
                });
    }

}