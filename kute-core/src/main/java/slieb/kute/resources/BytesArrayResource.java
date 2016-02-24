package slieb.kute.resources;


import slieb.kute.api.Resource;
import slieb.kute.internal.ImmutableBytesArray;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * A Resource object that contains everything in memory
 */
public class BytesArrayResource implements Resource.Readable {

    private final String path;

    private final ImmutableBytesArray immutableBytesArray;

    /**
     * @param path                The path of this resource.
     * @param immutableBytesArray The ImmutableByteArray for this resource.
     */
    public BytesArrayResource(String path, ImmutableBytesArray immutableBytesArray) {
        this.path = path;
        this.immutableBytesArray = immutableBytesArray;
    }

    /**
     * @param path  The path for this resource.
     * @param bytes Primitive byte array object for this resource.
     */
    public BytesArrayResource(String path, byte[] bytes) {
        this(path, new ImmutableBytesArray(bytes));
    }

    /**
     * @return A input stream that will supply the immutable byte array.
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(immutableBytesArray.getBytes());
    }

    /**
     * @return The path to this resource.
     */
    @Override
    public String getPath() {
        return path;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BytesArrayResource)) return false;
        BytesArrayResource that = (BytesArrayResource) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(immutableBytesArray, that.immutableBytesArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, immutableBytesArray);
    }

    @Override
    public String toString() {
        return "BytesArrayResource{" +
                "path='" + path + '\'' +
                ", immutableBytesArray=" + immutableBytesArray +
                '}';
    }
}
