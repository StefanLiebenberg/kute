package org.slieb.kute.api;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

public class TestUtils {

    public static final String EXPECTED_PATH = "/path";
    public static final String EXPECTED_CONTENT = "expected content";
    public static final String EXPECTED_MD5_CHECKSUM = "2F148DD6E5853DDCA4E69A2B8B6AB00B";
    public static final String EXPECTED_SHA_CHECKSUM = "A63AD6935048358180CE89BED6F87228DBE85EC6";

    public static Resource.Readable readableResource(String path,
                                                     String content) {
        return new Resource.Readable() {
            @Override
            public InputStream getInputStream() throws IOException {
                return IOUtils.toInputStream(content);
            }

            @Override
            public String getPath() {
                return path;
            }
        };
    }

    public static Resource.Writable writableResource(final String path,
                                                     final AtomicReference<StringWriter> reference) {
        return new Resource.Writable() {

            @Override
            public OutputStream getOutputStream() throws IOException {
                final StringWriter writer = new StringWriter();
                reference.set(writer);
                return new WriterOutputStream(writer, Charset.defaultCharset());
            }

            @Override
            public String getPath() {
                return path;
            }
        };
    }

    public static Resource.Checksumable checksumable(final String content) {
        return digest -> digest.update(content.getBytes());
    }
}
