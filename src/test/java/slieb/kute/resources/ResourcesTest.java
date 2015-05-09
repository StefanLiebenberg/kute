package slieb.kute.resources;


import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import slieb.kute.api.Resource;
import slieb.kute.resources.implementations.CachedResource;

import java.io.*;
import java.util.stream.IntStream;


public class ResourcesTest {

    @Test
    public void testReadResourceCorrectly() throws Exception {
        Resource.Readable readable = Mockito.mock(Resource.Readable.class);
        Mockito.when(readable.getReader()).thenAnswer(i -> new StringReader("content"));
        Assert.assertEquals("content", Resources.readResource(readable));
    }

    @Test(expected = RuntimeException.class)
    public void testReadResourceUnsafe() {
        Resources.readResourceUnsafe(new Resource.Readable() {
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
        Resource.Writeable writeable = Mockito.mock(Resource.Writeable.class);
        Mockito.when(writeable.getWriter()).thenReturn(writer);
        Resources.writeResource(writeable, "content");
        Assert.assertEquals("content", writer.toString());
    }

    @Test
    public void testCopyResource() throws Exception {
        Resource.Readable readable = Mockito.mock(Resource.Readable.class);
        Mockito.when(readable.getReader()).thenAnswer(i -> new StringReader("content"));

        StringWriter writer = new StringWriter();
        Resource.Writeable writeable = Mockito.mock(Resource.Writeable.class);
        Mockito.when(writeable.getWriter()).thenReturn(writer);
        Resources.copyResource(readable, writeable);
        Assert.assertEquals("content", writer.toString());
    }

    @Test
    public void testFileResource() throws Exception {


    }

    @Test
    public void testFileResource1() throws Exception {

    }

    @Test
    public void testRename() throws Exception {
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resource.getPath()).thenReturn("/path");
        Resource renamed = Resources.rename("/renamedPath", resource);
        Assert.assertEquals("/renamedPath", renamed.getPath());
    }

    @Test
    public void testInputStreamResource() throws Exception {
        try (InputStream inputStream = IOUtils.toInputStream("content")) {
            Resource.Readable readable = Resources.inputStreamResource("/path", () -> inputStream);
            Assert.assertEquals("content", Resources.readResource(readable));
            Assert.assertEquals("/path", readable.getPath());
        }
    }

    @Test
    public void testCacheResource() throws Exception {
        Resource.Readable readable = Mockito.mock(Resource.Readable.class);
        CachedResource cached = Resources.cacheResource(readable);

        Mockito.when(readable.getReader()).thenAnswer(invocationOnMock -> new StringReader("cache"));
        Assert.assertEquals("cache", Resources.readResource(cached));

        IntStream.range(0, 10000)
                .parallel()
                .forEach(i -> {
                    try {
                        Mockito.when(readable.getReader()).thenAnswer(invocationOnMock -> new StringReader("clear"));
                        Assert.assertEquals("cache", Resources.readResource(cached));
                    } catch (IOException io) {
                        throw new RuntimeException(io);
                    }
                });

        cached.clear();
        Assert.assertEquals("clear", Resources.readResource(cached));

    }

    @Test
    public void testStringResource() throws Exception {
        final Resource.Readable readable = Resources.stringResource("/path", "content");
        IntStream.range(0, 10000)
                .parallel()
                .forEach(i -> {
                    try {
                        Assert.assertEquals("content", Resources.readResource(readable));
                        Assert.assertEquals("/path", readable.getPath());
                    } catch (IOException ignored) {
                        throw new RuntimeException(ignored);
                    }
                });
    }

  
}