package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class RenamedPathResource<A extends Resource> implements Resource.Readable, Resource.Writeable {

    private final A resource;

    private final String path;

    public RenamedPathResource(A resource, String path) {
        this.resource = resource;
        this.path = path;
    }

    @Override
    public Reader getReader() throws IOException {
        if (resource instanceof Readable) {
            return ((Readable) resource).getReader();
        }
        throw new IllegalStateException("resource not instance of Resource.Readable");
    }

    @Override
    public Writer getWriter() throws IOException {
        if (resource instanceof Writeable) {
            return ((Writeable) resource).getWriter();
        }
        throw new IllegalStateException("resource not instance of Resource.Writeable");
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RenamedPathResource)) return false;

        RenamedPathResource that = (RenamedPathResource) o;

        if (!path.equals(that.path)) return false;
        if (!resource.equals(that.resource)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
