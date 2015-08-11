package org.slieb.kute.service.resources;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


public abstract class AbstractResource implements ServiceResource, Resource.Readable {

    private final String path;

    public AbstractResource(String path) {
        this.path = path;
    }

    public abstract String getContent() throws IOException;

    @Override
    public Reader getReader() throws IOException {
        return new StringReader(getContent());
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }
}
