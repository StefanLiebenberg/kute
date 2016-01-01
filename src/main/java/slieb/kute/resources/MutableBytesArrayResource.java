package slieb.kute.resources;


import slieb.kute.api.Resource;
import slieb.kute.utils.internal.MutableBytesArray;

import java.io.*;
import java.util.Objects;

public class MutableBytesArrayResource implements Resource.Readable, Resource.Writable {

    private final String path;
    private final MutableBytesArray bytes;

    public MutableBytesArrayResource(String path, byte[] bytes) {
        this.path = path;
        this.bytes = new MutableBytesArray(bytes);
    }

    public MutableBytesArrayResource(String path) {
        this.path = path;
        this.bytes = new MutableBytesArray();
    }

    public void setBytes(byte[] bytes) {
        this.bytes.setBytes(bytes);
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes.getBytes());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new SyncedOutputStream();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableBytesArrayResource)) return false;
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


    private class SyncedOutputStream extends OutputStream {

        private final ByteArrayOutputStream bufferOutputStream;

        private SyncedOutputStream() {
            bufferOutputStream = new ByteArrayOutputStream();
        }

        @Override
        public void write(int b) throws IOException {
            bufferOutputStream.write(b);
        }

        @Override
        public void close() throws IOException {
            setBytes(bufferOutputStream.toByteArray());
            bufferOutputStream.close();
        }
    }


}

