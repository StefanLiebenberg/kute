package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Objects;

public class RenamedPathResource implements Resource.Readable, Serializable {

    private final String path;
    private final Resource.Readable readable;

    public RenamedPathResource(String path, Readable readable) {
        this.path = path;
        this.readable = readable;
    }

    public Resource.Readable getResource() {
        return readable;
    }

    @Override
    public Reader getReader() throws IOException {
        return readable.getReader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return readable.getInputStream();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "RenamedPathResource{" +
                "path='" + path + '\'' +
                ", readable=" + readable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RenamedPathResource)) return false;
        RenamedPathResource that = (RenamedPathResource) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(readable, that.readable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, readable);
    }
}
