package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static slieb.kute.resources.Resources.readResource;

public class CachedResource extends AbstractResource implements Resource.Proxy<Resource.Readable> {

    private final Readable resource;
    private String cachedValue;
    private boolean cached;

    public CachedResource(Readable resource) {
        this.resource = resource;
        this.cached = false;
    }

    @Override
    public Readable getResource() {
        return resource;
    }

    @Override
    public synchronized Reader getReader() throws IOException {
        if (!cached) {
            cached = true;
            cachedValue = readResource(resource);
        }
        return new StringReader(cachedValue);
    }

    @Override
    public String getPath() {
        return resource.getPath();
    }

    public void clear() {
        cached = false;
        cachedValue = null;
    }
}
