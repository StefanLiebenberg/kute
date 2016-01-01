package slieb.kute.utils;

import java.io.Serializable;
import java.util.Arrays;

public class MutableBytesArray implements Serializable {

    private byte[] internalBytes;

    public MutableBytesArray(byte[] internalBytes) {
        this.internalBytes = internalBytes;
    }

    public MutableBytesArray() {
        this.internalBytes = new byte[0];
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
        if (!(o instanceof MutableBytesArray)) return false;
        MutableBytesArray bytes = (MutableBytesArray) o;
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
