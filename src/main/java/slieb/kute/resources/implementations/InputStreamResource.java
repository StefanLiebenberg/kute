package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Supplier;


public class InputStreamResource extends AbstractResource
        implements Resource.Readable, Resource.InputStreaming {

    private final Supplier<InputStream> inputStream;
    private final String path;

    public InputStreamResource(Supplier<InputStream> inputStream, String path) {
        this.inputStream = inputStream;
        this.path = path;
    }

    @Override
    public Reader getReader() {
        return new InputStreamReader(getInputStream());
    }

    @Override
    public InputStream getInputStream() {
        return inputStream.get();
    }

    @Override
    public String getPath() {
        return path;
    }

}
