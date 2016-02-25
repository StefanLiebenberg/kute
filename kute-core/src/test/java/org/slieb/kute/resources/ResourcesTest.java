package org.slieb.kute.resources;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slieb.kute.Kute;
import org.slieb.kute.KuteFactory;
import org.slieb.kute.KuteIO;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.slieb.kute.Kute.*;
import static org.slieb.kute.KuteIO.*;

public class ResourcesTest {

    @Test
    public void testReadResourceCorrectly() throws Exception {
        Resource.Readable readable = mock(Resource.Readable.class);
        when(readable.getReader()).thenAnswer(i -> new StringReader("content"));
        assertEquals("content", readResource(readable));
    }

    @Test
    public void testWriteResource() throws Exception {
        MutableBytesArrayResource mutableResource = KuteFactory.mutableResource("/path", "");
        writeResource(mutableResource, "content");
        assertEquals("content", KuteIO.readResource(mutableResource));
    }

    @Test
    public void testCopyResource() throws Exception {
        Resource.Readable readable = Kute.stringResource("/path", "content");
        MutableBytesArrayResource writable = KuteFactory.mutableResource("/otherpath", "");
        copyResource(readable, writable);
        assertEquals("content", KuteIO.readResource(writable));
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
            Resource.Readable readable = KuteFactory.inputStreamResource("/path", () -> inputStream);
            assertEquals("content", readResource(readable));
            assertEquals("/path", readable.getPath());
        }
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