package slieb.kute.resources;


import slieb.kute.api.Resource;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class MutableResource implements Resource.Readable, Resource.Writable {

    private final String path;
    private final Bytes bytes;

    public MutableResource(String path, byte[] bytes) {
        this.path = path;
        this.bytes = new Bytes(bytes);
    }

    public MutableResource(String path) {
        this.path = path;
        this.bytes = new Bytes(new byte[]{});
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
        if (!(o instanceof MutableResource)) return false;
        MutableResource that = (MutableResource) o;
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

        private final ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();

        public SyncedOutputStream() {
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

    private class Bytes implements Serializable {

        private byte[] internalBytes;

        public Bytes(byte[] internalBytes) {
            this.internalBytes = internalBytes;
        }

        public synchronized void setBytes(byte[] bytes) {
            this.internalBytes = bytes;
        }

        public synchronized byte[] getBytes() {
            return internalBytes;
        }

        @Override
        public synchronized boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Bytes)) return false;
            Bytes bytes = (Bytes) o;
            return Arrays.equals(internalBytes, bytes.internalBytes);
        }

        @Override
        public synchronized int hashCode() {
            return Arrays.hashCode(internalBytes);
        }

        @Override
        public synchronized String toString() {
            return "Bytes{" +
                    "internalBytes=" + Arrays.toString(internalBytes) +
                    '}';
        }
    }
}

