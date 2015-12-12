package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static slieb.kute.Kute.readResource;

public class CachedResource extends AbstractProxy<Resource.Readable> implements Resource.Proxy<Resource.Readable> {

    private String cachedValue;
    private boolean cached;

    public CachedResource(Readable resource) {
        super(resource);
        this.cached = false;
    }

    @Override
    public synchronized Reader getReader() throws IOException {
        if (!cached) {
            cached = true;
            cachedValue = readResource(getResource());
        }
        return new StringReader(cachedValue);
    }

    public void clear() {
        cached = false;
        cachedValue = null;
    }
}
