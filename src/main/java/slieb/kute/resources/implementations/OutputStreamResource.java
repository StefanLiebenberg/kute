package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.OutputStream;
import java.util.function.Supplier;

/**
 *
 */
public class OutputStreamResource extends AbstractResource implements Resource.OutputStreaming {

    private final Supplier<OutputStream> outputStream;

    private final String path;

    public OutputStreamResource(Supplier<OutputStream> outputStream, String path) {
        this.outputStream = outputStream;
        this.path = path;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream.get();
    }

    @Override
    public String getPath() {
        return path;
    }
}
