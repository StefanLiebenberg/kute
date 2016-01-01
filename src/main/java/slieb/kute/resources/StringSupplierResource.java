package slieb.kute.resources;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;


public class StringSupplierResource implements ContentResource {

    private final String path;
    private final Supplier<String> supplier;

    public StringSupplierResource(String path, Supplier<String> supplier) {
        this.path = path;
        this.supplier = supplier;
    }

    @Override
    public String getContent() throws IOException {
        return supplier.get();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringSupplierResource)) return false;
        StringSupplierResource that = (StringSupplierResource) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, supplier);
    }

    @Override
    public String toString() {
        return "StringSupplierResource{" +
                "path='" + path + '\'' +
                ", supplier=" + supplier +
                '}';
    }
}
