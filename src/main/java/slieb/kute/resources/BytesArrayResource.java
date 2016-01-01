package slieb.kute.resources;


import slieb.kute.api.Resource;
import slieb.kute.utils.internal.ImmutableBytesArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class BytesArrayResource implements Resource.Readable {

    private final String path;

    private final ImmutableBytesArray immutableBytesArray;


    public BytesArrayResource(String path, ImmutableBytesArray immutableBytesArray) {
        this.path = path;
        this.immutableBytesArray = immutableBytesArray;
    }

    public BytesArrayResource(String path, byte[] bytes) {
        this(path, new ImmutableBytesArray(bytes));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(immutableBytesArray.getBytes());
    }

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
