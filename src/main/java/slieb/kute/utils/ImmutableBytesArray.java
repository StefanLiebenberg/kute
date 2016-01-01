package slieb.kute.utils;

import java.io.Serializable;
import java.util.Arrays;


public class ImmutableBytesArray implements Serializable {

    private final byte[] internalBytes;

    public ImmutableBytesArray(byte[] internalBytes) {
        this.internalBytes = internalBytes;
    }

    public byte[] getBytes() {
        return internalBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImmutableBytesArray)) return false;
        ImmutableBytesArray that = (ImmutableBytesArray) o;
        return Arrays.equals(internalBytes, that.internalBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(internalBytes);
    }

    @Override
    public String toString() {
        return "ImmutableBytesArray{" +
                "internalBytes=" + Arrays.toString(internalBytes) +
                '}';
    }
}

