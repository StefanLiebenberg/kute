package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Supplier;


public class InputStreamResource extends AbstractResource
        implements Resource.Readable {

    private final Supplier<InputStream> inputStream;
    private final String path;

    public InputStreamResource(Supplier<InputStream> inputStream, String path) {
        this.inputStream = inputStream;
        this.path = path;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(this.inputStream.get());
    }

    @Override
    public String getPath() {
        return path;
    }

}
