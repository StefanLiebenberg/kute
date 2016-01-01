package slieb.kute.resources;

import slieb.kute.api.Resource;
import slieb.kute.utils.interfaces.SupplierWithIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;


public class InputStreamResource implements Resource.Readable, Serializable {

    private final String path;
    private final SupplierWithIO<InputStream> supplier;

    public InputStreamResource(final String path,
                               final SupplierWithIO<InputStream> resourceSupplierWithIO) {
        this.path = path;
        this.supplier = resourceSupplierWithIO;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return supplier.getWithIO();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputStreamResource)) return false;
        InputStreamResource that = (InputStreamResource) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, supplier);
    }

    @Override
    public String toString() {
        return "InputStreamResource{" +
                "path='" + path + '\'' +
                ", supplier=" + supplier +
                '}';
    }

}

