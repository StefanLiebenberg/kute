package slieb.kute.utils;

import java.io.IOException;


@FunctionalInterface
public interface SupplierWithIO<T> {
    T getWithIO() throws IOException;

    static SupplierWithIO<byte[]> ofInstance(byte[] bytes) {
        return () -> bytes;
    }
}

