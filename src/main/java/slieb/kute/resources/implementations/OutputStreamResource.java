package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.OutputStream;
import java.util.function.Supplier;

/**
 * The output stream resource.
 */
public class OutputStreamResource extends AbstractResource implements Resource.OutputStreaming {

    private final Supplier<OutputStream> supplier;

    public OutputStreamResource(final String path,
                                final Supplier<OutputStream> supplier) {
        super(path);
        this.supplier = supplier;
    }


    @Override
    public OutputStream getOutputStream() {
        return supplier.get();
    }

}
