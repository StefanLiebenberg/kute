package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;


public class InputStreamResource extends AbstractResource
        implements Resource.Readable, Resource.InputStreaming {

    private final SupplierWithIO<InputStream> supplier;

    public InputStreamResource(String path, SupplierWithIO<InputStream> supplierWithIO) {
        super(path);
        this.supplier = supplierWithIO;
    }

    public InputStreamResource(String path, Supplier<InputStream> inputStream) {
        this(path, (SupplierWithIO<InputStream>) inputStream::get);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return supplier.getWithIO();
    }
    
    @FunctionalInterface
    public interface SupplierWithIO<T> {
        T getWithIO() throws IOException;
    }

}

