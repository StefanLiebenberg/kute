package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;


public class StringResource extends AbstractResource implements Resource.Readable {
    private final String string;

    private final String path;

    public StringResource(String string, String path) {
        this.string = string;
        this.path = path;
    }

    private StringWriter writer;

    @Override
    public StringReader getReader() throws IOException {
        return new StringReader(string);
    }


    @Override
    public String getPath() {
        return path;
    }
}
