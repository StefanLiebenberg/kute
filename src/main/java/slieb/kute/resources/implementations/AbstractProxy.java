package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.*;

import static slieb.kute.resources.Resources.getResourceAs;


public abstract class AbstractProxy<R extends Resource> extends AbstractResource implements Resource.Proxy<R> {

    private final R resource;

    public AbstractProxy(R resource) {
        this(resource.getPath(), resource);
    }

    public AbstractProxy(String path, R resource) {
        super(path);
        this.resource = resource;
    }

    public R getResource() {
        return resource;
    }

    @Override
    public Reader getReader() throws IOException {
        return getResourceAs(getResource(), Resource.Readable.class).getReader();
    }

    @Override
    public Writer getWriter() throws IOException {
        return getResourceAs(getResource(), Resource.Writeable.class).getWriter();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getResourceAs(getResource(), Resource.InputStreaming.class).getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return getResourceAs(getResource(), Resource.OutputStreaming.class).getOutputStream();
    }
}
