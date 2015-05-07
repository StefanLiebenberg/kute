package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.function.Supplier;

/**
 * Warning, this is a use once resource. Avoid if possible.
 */
public class OutputStreamResource
        extends AbstractResource
        implements Resource.Writeable, Resource.OutputStreaming {

    private final Supplier<OutputStream> outputStream;

    private final String path;

    public OutputStreamResource(Supplier<OutputStream> outputStream, String path) {
        this.outputStream = outputStream;
        this.path = path;
    }

    @Override
    public Writer getWriter() {
        return new OutputStreamWriter(getOutputStream());
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
