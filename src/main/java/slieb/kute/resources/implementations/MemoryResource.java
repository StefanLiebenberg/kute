package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;


public class MemoryResource implements Resource.Readable, Resource.Writeable {

    private final String path;
    private StringWriter writer;

    public MemoryResource(String path) {
        this.path = path;
        this.writer = new StringWriter();
    }

    @Override
    public synchronized StringReader getReader() throws IOException {
        return new StringReader(writer.toString());
    }

    @Override
    public synchronized StringWriter getWriter() throws IOException {
        writer.close();
        writer = new StringWriter();
        return writer;
    }

    @Override
    public String getPath() {
        return path;
    }
}
