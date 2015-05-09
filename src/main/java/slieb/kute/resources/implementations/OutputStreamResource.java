package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.OutputStream;
import java.util.function.Supplier;

/**
 *
 */
public class OutputStreamResource extends AbstractResource implements Resource.OutputStreaming {

    private final Supplier<OutputStream> supplier;

    public OutputStreamResource(Supplier<OutputStream> supplier, String path) {
        super(path);
        this.supplier = supplier;
    }

    @Override
    public OutputStream getOutputStream() {
        return supplier.get();
    }

}
