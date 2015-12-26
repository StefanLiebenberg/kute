package slieb.kute.api;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.junit.Test;
import org.mockito.Mockito;
import slieb.kute.utils.KuteDigest;
import slieb.kute.utils.KuteIO;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ResourceTest {

    @Test
    public void testOutputStreamingApiImplementsDefaultMethod() throws Exception {

        final ByteArrayOutputStream mockOutputStream = new ByteArrayOutputStream();
        Resource.Writable outputStreaming = new Resource.Writable() {

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
        KuteIO.writeResource(outputStreaming, value);
        assertEquals(mockOutputStream.toString(Charset.defaultCharset().name()), value);
    }

    @Test
    public void testInputStreamingApiImplementsDefaultGetReaderMethod() throws Exception {

        final String value = "VALUE_STRING";
        final Resource.Readable inputStreaming = new Resource.Readable() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(value.getBytes(Charset.defaultCharset()));
            }

            @Override
            public String getPath() {
                return "/path";
            }
        };
        assertEquals(value, KuteIO.readResource(inputStreaming));
    }

    @Test
    public void testChecksumOfResource() {
        Resource.Readable readable = new Resource.Readable() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("hello world".getBytes());
            }

            @Override
            public String getPath() {
                return "/hi";
            }
        };
        assertEquals("5EB63BBBE01EEED093CB22BB8F5ACDC3", HexBin.encode(KuteDigest.md5(readable)));
    }

    @Test
    public void testChecksumOfProvider() {

        Resource.Readable readableA = new Resource.Readable() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("resourceA".getBytes());
            }

            @Override
            public String getPath() {
                return "/hi";
            }
        };

        Resource.Readable readableB = new Resource.Readable() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("resourceB".getBytes());
            }

            @Override
            public String getPath() {
                return "/hi";
            }
        };

        Resource.Provider provider = () -> Stream.of(readableA, readableB);
        assertEquals("BFF3C70625AD8F52446BC7BA08F45FB2", HexBin.encode(KuteDigest.md5(provider)));
    }


//    @Test
//    public void testProxyResourceImplementsDefaultGetReader() throws Exception {
//        final Resource.Readable readable = getMockReadable();
//        final Resource.Proxy proxy = getProxyResource(readable);
//        assertNotNull(proxy.getReader());
//    }


//    @Test(expected = IllegalStateException.class)
//    public void testProxyResourceThrowsErrorWhenAskingForWriterOfNonWritable() throws Exception {
//        final Resource.Readable readable = getMockReadable();
//        final Resource.Proxy proxy = getProxyResource(readable);
//        proxy.getWriter();
//    }

//    @Test
//    public void testProxyResourceImplementsDefaultGetWriter() throws Exception {
//        final Resource.Writable writable = getMockWritable();
//        final Resource.Proxy proxy = getProxyResource(writable);
//        assertNotNull(proxy.getWriter());
//    }


//    @Test(expected = IllegalStateException.class)
//    public void testProxyResourceThrowsErrorWhenAskingForReaderOfNonReadable() throws Exception {
//        final Resource.Writable writable = getMockWritable();
//        final Resource.Proxy proxy = getProxyResource(writable);
//        proxy.getReader();
//    }

//    @Test(expected = IllegalStateException.class)
//    public void testProxyResourceThrowsErrorWhenAskingForInputStreamOfNonInputStreaming() throws Exception {
//        final Resource.Readable readable = getMockReadable();
//        final Resource.Proxy proxy = getProxyResource(readable);
//        proxy.getInputStream();
//    }

//    @Test(expected = IllegalStateException.class)
//    public void testProxyResourceThrowsErrorWhenAskingForOutputStreamOfNonOutputStreaming() throws Exception {
//        final Resource.Writable writable = getMockWritable();
//        final Resource.Proxy proxy = getProxyResource(writable);
//        proxy.getOutputStream();
//    }

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

            @Override
            public InputStream getInputStream() throws IOException {
                return Mockito.mock(InputStream.class);
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

            @Override
            public OutputStream getOutputStream() throws IOException {
                return Mockito.mock(OutputStream.class);
            }
        };
    }


}