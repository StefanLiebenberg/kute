package slieb.kute.utils;

import slieb.kute.utils.interfaces.SupplierWithIO;

import java.io.IOException;
import java.util.Arrays;


public class BytesSupplier implements SupplierWithIO<byte[]> {

    private final byte[] bytes;

    public BytesSupplier(byte[] bytes) {
        this.bytes = bytes;
    }


    @Override
    public byte[] getWithIO() throws IOException {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BytesSupplier)) return false;
        BytesSupplier that = (BytesSupplier) o;
        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return "BytesSupplier{" +
                "bytes=" + Arrays.toString(bytes) +
                '}';
    }
}
