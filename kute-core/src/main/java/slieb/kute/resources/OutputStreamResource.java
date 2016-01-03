package slieb.kute.resources;


import org.slieb.throwables.SupplierWithThrowable;
import slieb.kute.api.Resource;

import java.io.*;

/**
 * The output stream resource.
 */
public class OutputStreamResource implements Resource.Writable, Serializable {


    private final String path;
    private final SupplierWithThrowable<OutputStream, IOException> supplier;

    public OutputStreamResource(final String path,
                                final SupplierWithThrowable<OutputStream, IOException> supplier) {
        this.path = path;
        this.supplier = supplier;
    }


    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(getOutputStream());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return supplier.getWithThrowable();
    }

    @Override
    public String getPath() {
        return path;
    }
}
