package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A Resource object that contains everything in memory
 */
public class BytesArrayResource implements Resource.Readable {

    private final String path;

    private final byte[] bytes;

    /**
     * @param path  The path of this resource.
     * @param bytes The bytes;
     */
    public BytesArrayResource(final String path,
                              final byte[] bytes) {
        this.path = path;
        this.bytes = bytes;
    }

    /**
     * @return A input stream that will supply the immutable byte array.
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * @return The path to this resource.
     */
    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "BytesArrayResource{" +
                "bytes=" + Arrays.toString(bytes) +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof BytesArrayResource)) { return false; }

        final BytesArrayResource that = (BytesArrayResource) o;

        if (path != null ? !path.equals(that.path) : that.path != null) { return false; }
        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
