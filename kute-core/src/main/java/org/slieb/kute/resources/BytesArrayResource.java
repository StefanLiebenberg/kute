package org.slieb.kute.resources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A Readonly resource that contains everything in memory.
 */
public class BytesArrayResource extends AbstractResource {

    private final byte[] bytes;

    /**
     * @param path    The resource path.
     * @param bytes   The resource bytes.
     * @param charset The resource charset.
     */
    public BytesArrayResource(final String path,
                              final byte[] bytes,
                              final Charset charset) {
        super(path, charset);
        this.bytes = bytes;
    }

    /**
     * @param path  The resource path.
     * @param bytes The resource bytes.
     */
    public BytesArrayResource(final String path,
                              final byte[] bytes) {
        this(path, bytes, Charset.defaultCharset());
    }

    /**
     * @return The resource input stream.
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof BytesArrayResource)) { return false; }
        if (!super.equals(o)) { return false; }

        final BytesArrayResource that = (BytesArrayResource) o;

        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        return "BytesArrayResource{" +
                "bytes=" + Arrays.toString(bytes) +
                "} " + super.toString();
    }
}
