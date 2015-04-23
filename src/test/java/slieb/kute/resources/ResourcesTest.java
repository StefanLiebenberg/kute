package slieb.kute.resources;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import slieb.kute.api.Resource;

import java.io.StringReader;
import java.io.StringWriter;


public class ResourcesTest {

    @Test
    public void testReadResourceCorrectly() throws Exception {
        Resource.Readable readable = Mockito.mock(Resource.Readable.class);
        Mockito.when(readable.getReader()).thenAnswer(i -> new StringReader("content"));
        Assert.assertEquals("content", Resources.readResource(readable));
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

    }

    @Test
    public void testInputStreamResource() throws Exception {

    }

    @Test
    public void testCacheResource() throws Exception {

    }

    @Test
    public void testStringResource() throws Exception {

    }

    @Test
    public void testUrlResource() throws Exception {

    }

    @Test
    public void testZipEntryResource() throws Exception {

    }

    @Test
    public void testZipEntryResource1() throws Exception {

    }

    @Test
    public void testResourceProviderToList() throws Exception {

    }

    @Test
    public void testResourceProviderToSet() throws Exception {

    }

    @Test
    public void testMapResources() throws Exception {

    }

    @Test
    public void testFilterResources() throws Exception {

    }

    @Test
    public void testFilterResources1() throws Exception {

    }

    @Test
    public void testProviderOf() throws Exception {

    }

    @Test
    public void testProviderOf1() throws Exception {

    }

    @Test
    public void testGroup() throws Exception {

    }
}