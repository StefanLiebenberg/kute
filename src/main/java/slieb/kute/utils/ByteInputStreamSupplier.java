package slieb.kute.utils;

import slieb.kute.utils.interfaces.SupplierWithIO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class ByteInputStreamSupplier implements SupplierWithIO<InputStream> {

    private SupplierWithIO<byte[]> byteSupplier;

    public ByteInputStreamSupplier(SupplierWithIO<byte[]> byteSupplier) {
        this.byteSupplier = byteSupplier;
    }

    @Override
    public InputStream getWithIO() throws IOException {
        return new ByteArrayInputStream(byteSupplier.getWithIO());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ByteInputStreamSupplier)) return false;
        ByteInputStreamSupplier that = (ByteInputStreamSupplier) o;
        return Objects.equals(byteSupplier, that.byteSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(byteSupplier);
    }

    @Override
    public String toString() {
        return "ByteInputStreamSupplier{" +
                "byteSupplier=" + byteSupplier +
                '}';
    }
}
