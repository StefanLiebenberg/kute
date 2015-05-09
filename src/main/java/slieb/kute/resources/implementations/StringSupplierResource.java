package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.function.Supplier;

public class StringSupplierResource extends AbstractResource implements Resource.Readable {

    private final Supplier<String> supplier;

    public StringSupplierResource(String path, Supplier<String> supplier) {
        super(path);
        this.supplier = supplier;
    }

    public StringSupplierResource(String path, String content) {
        this(path, () -> content);
    }

    @Override
    public Reader getReader() throws IOException {
        return new StringReader(supplier.get());
    }

}
