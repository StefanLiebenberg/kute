package slieb.kute.resources;


import slieb.kute.api.Resource;
import slieb.kute.utils.KuteIO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class CachedResource implements Resource.Readable, Serializable {

    private byte[] cachedValue;
    private boolean cached;
    private final Resource.Readable resource;

    public CachedResource(Resource.Readable resource) {
        this.resource = resource;
        this.cached = false;
    }

    private synchronized byte[] getCachedValue() throws IOException {
        if (!cached) {
            cached = true;
            cachedValue = KuteIO.readBytes(resource);
        }
        return cachedValue;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(getCachedValue());
    }

    public void clear() {
        cached = false;
        cachedValue = null;
    }

    @Override
    public String getPath() {
        return resource.getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CachedResource)) return false;
        CachedResource that = (CachedResource) o;
        return cached == that.cached &&
                Arrays.equals(cachedValue, that.cachedValue) &&
                Objects.equals(resource, that.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cachedValue, cached, resource);
    }

    @Override
    public String toString() {
        return "CachedResource{" +
                "cachedValue=" + Arrays.toString(cachedValue) +
                ", cached=" + cached +
                ", resource=" + resource +
                '}';
    }
}
