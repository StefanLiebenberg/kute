package slieb.kute.api;

import org.junit.Test;
import org.mockito.Mockito;
import slieb.kute.Kute;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResourceTest {

    @Test
    public void testOutputStreamingApiImplementsDefaultMethod() throws Exception {

        final ByteArrayOutputStream mockOutputStream = new ByteArrayOutputStream();
        Resource.OutputStreaming outputStreaming = new Resource.OutputStreaming() {

            @Override
            public OutputStream getOutputStream() throws IOException {
                return mockOutputStream;
            }

            @Override
            public String getPath() {
                return "/path";
            }
        };
        String value = "valueStuff";
        Kute.writeResource(outputStreaming, value);
        assertEquals(mockOutputStream.toString(Charset.defaultCharset().name()), value);
    }

    @Test
    public void testInputStreamingApiImplementsDefaultGetReaderMethod() throws Exception {

        final String value = "VALUE_STRING";
        final Resource.InputStreaming inputStreaming = new Resource.InputStreaming() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(value.getBytes(Charset.defaultCharset()));
            }

            @Override
            public String getPath() {
                return "/path";
            }
        };
        assertEquals(value, Kute.readResource(inputStreaming));
    }


    @Test
    public void testProxyResourceImplementsDefaultGetReader() throws Exception {
        final Resource.Readable readable = getMockReadable();
        final Resource.Proxy proxy = getProxyResource(readable);
        assertNotNull(proxy.getReader());
    }


    @Test(expected = IllegalStateException.class)
    public void testProxyResourceThrowsErrorWhenAskingForWriterOfNonWritable() throws Exception {
        final Resource.Readable readable = getMockReadable();
        final Resource.Proxy proxy = getProxyResource(readable);
        proxy.getWriter();
    }

    @Test
    public void testProxyResourceImplementsDefaultGetWriter() throws Exception {
        final Resource.Writable writable = getMockWritable();
        final Resource.Proxy proxy = getProxyResource(writable);
        assertNotNull(proxy.getWriter());
    }


    @Test(expected = IllegalStateException.class)
    public void testProxyResourceThrowsErrorWhenAskingForReaderOfNonReadable() throws Exception {
        final Resource.Writable writable = getMockWritable();
        final Resource.Proxy proxy = getProxyResource(writable);
        proxy.getReader();
    }

    @Test(expected = IllegalStateException.class)
    public void testProxyResourceThrowsErrorWhenAskingForInputStreamOfNonInputStreaming() throws Exception {
        final Resource.Readable readable = getMockReadable();
        final Resource.Proxy proxy = getProxyResource(readable);
        proxy.getInputStream();
    }

    @Test(expected = IllegalStateException.class)
    public void testProxyResourceThrowsErrorWhenAskingForOutputStreamOfNonOutputStreaming() throws Exception {
        final Resource.Writable writable = getMockWritable();
        final Resource.Proxy proxy = getProxyResource(writable);
        proxy.getOutputStream();
    }

    private static Resource.Readable getMockReadable() {
        return new Resource.Readable() {
            @Override
            public String getPath() {
                return "/path";
            }

            @Override
            public Reader getReader() throws IOException {
                return Mockito.mock(Reader.class);
            }
        };
    }

    private static Resource.Writable getMockWritable() {
        return new Resource.Writable() {
            @Override
            public String getPath() {
                return "/path";
            }

            @Override
            public Writer getWriter() throws IOException {
                return Mockito.mock(Writer.class);
            }
        };
    }


    private static Resource.Proxy getProxyResource(final Resource readable) {
        return new Resource.Proxy() {

            @Override
            public Resource getResource() {
                return readable;
            }

            @Override
            public String getPath() {
                return readable.getPath();
            }
        };
    }


}