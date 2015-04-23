package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static slieb.kute.resources.Resources.readResource;

public class MemoryCacheResource implements Resource.Readable {

    private final Readable readable;
    private String cachedValue;
    private boolean cached;

    public MemoryCacheResource(Readable readable) {
        this.readable = readable;
        this.cached = false;
    }


    @Override
    public synchronized Reader getReader() throws IOException {
        if (!cached) {
            cached = true;
            cachedValue = readResource(readable);
        }
        return new StringReader(cachedValue);
    }


    @Override
    public String getPath() {
        return readable.getPath();
    }
}
