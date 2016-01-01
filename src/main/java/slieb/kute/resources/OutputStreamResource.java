package slieb.kute.resources;


import slieb.kute.api.Resource;
import slieb.kute.api.SupplierWithIO;

import java.io.*;

/**
 * The output stream resource.
 */
public class OutputStreamResource implements Resource.Writable, Serializable {


    private final String path;
    private final SupplierWithIO<OutputStream> supplier;

    public OutputStreamResource(final String path,
                                final SupplierWithIO<OutputStream> supplier) {
        this.path = path;
        this.supplier = supplier;
    }


    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(getOutputStream());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return supplier.getWithIO();
    }

    @Override
    public String getPath() {
        return path;
    }
}
