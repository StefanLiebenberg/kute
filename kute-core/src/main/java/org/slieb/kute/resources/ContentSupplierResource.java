package org.slieb.kute.resources;

import org.slieb.throwables.SupplierWithThrowable;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * A resource that serves a supplied string.
 */
public class ContentSupplierResource extends AbstractResource implements ContentResource {

    private final SupplierWithThrowable<String, IOException> supplier;

    /**
     * @param path     The path on which this resource exists.
     * @param supplier The supplier of resource string.
     */
    public ContentSupplierResource(final String path,
                                   final SupplierWithThrowable<String, IOException> supplier) {
        super(path);
        this.supplier = supplier;
    }

    /**
     * @param path    The resource path.
     * @param charset The resource charset.
     */
    public ContentSupplierResource(final String path,
                                   final Charset charset,
                                   final SupplierWithThrowable<String, IOException> supplier) {
        super(path, charset);
        this.supplier = supplier;
    }

    /**
     * @return The resource content.
     * @throws IOException an IOException.
     */
    @Override
    public final String getContent() throws IOException {
        return supplier.getWithThrowable();
    }
}
