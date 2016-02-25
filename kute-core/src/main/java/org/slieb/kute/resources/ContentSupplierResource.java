package org.slieb.kute.resources;

import org.slieb.throwables.SupplierWithThrowable;

import java.io.IOException;

public class ContentSupplierResource implements ContentResource {

    private final String path;
    private final SupplierWithThrowable<String, IOException> supplier;

    public ContentSupplierResource(final String path,
                                   final SupplierWithThrowable<String, IOException> supplier) {
        this.path = path;
        this.supplier = supplier;
    }

    @Override
    public final String getContent() throws IOException {
        return supplier.getWithThrowable();
    }

    @Override
    public final String getPath() {
        return path;
    }
}
