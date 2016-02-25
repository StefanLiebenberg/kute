package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;
import org.slieb.kute.internal.SyncedOutputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MutableBytesArrayResource implements Resource.Readable, Resource.Writable {

    private final String path;

    private final AtomicReference<byte[]> bytes;

    public MutableBytesArrayResource(final String path,
                                     final AtomicReference<byte[]> bytes) {
        this.path = path;
        this.bytes = bytes;
    }

    public MutableBytesArrayResource(final String path,
                                     final byte[] bytes) {
        this(path, new AtomicReference<>(bytes));
    }

    public MutableBytesArrayResource(String path) {
        this(path, new AtomicReference<>());
    }

    public void setBytes(byte[] bytes) {
        this.bytes.set(bytes);
    }

    public void setContent(String content) {
        setBytes(content.getBytes(getCharset()));
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes.get());
    }

    @Override
    public OutputStream getOutputStream() {
        return new SyncedOutputStream(bytes);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof MutableBytesArrayResource)) { return false; }
        MutableBytesArrayResource that = (MutableBytesArrayResource) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, bytes);
    }

    @Override
    public String toString() {
        return "MutableResource{" +
                "path='" + path + '\'' +
                ", bytes=" + bytes +
                '}';
    }
}

