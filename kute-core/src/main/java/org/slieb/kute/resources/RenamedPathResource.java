package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

public class RenamedPathResource implements Resource.Readable {

    private final String path;
    private final Resource.Readable readable;

    public RenamedPathResource(String path,
                               Readable readable) {
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
    public Charset getCharset() {
        return readable.getCharset();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof RenamedPathResource)) { return false; }

        final RenamedPathResource that = (RenamedPathResource) o;

        if (path != null ? !path.equals(that.path) : that.path != null) { return false; }
        return readable != null ? readable.equals(that.readable) : that.readable == null;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (readable != null ? readable.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RenamedPathResource{" +
                "path='" + path + '\'' +
                ", readable=" + readable +
                '}';
    }
}
