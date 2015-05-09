package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.InputStream;
import java.util.function.Supplier;


public class InputStreamResource extends AbstractResource
        implements Resource.Readable, Resource.InputStreaming {

    private final Supplier<InputStream> supplier;

    public InputStreamResource(Supplier<InputStream> inputStream, String path) {
        super(path);
        this.supplier = inputStream;
    }

    @Override
    public InputStream getInputStream() {
        return supplier.get();
    }
    
}
