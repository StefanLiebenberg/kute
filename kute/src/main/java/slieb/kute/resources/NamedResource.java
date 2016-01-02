package slieb.kute.resources;

import slieb.kute.api.Resource;

import java.util.Objects;


public class NamedResource implements Resource {

    private final String path;

    public NamedResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedResource)) return false;
        NamedResource that = (NamedResource) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "NamedResource{" +
                "path='" + path + '\'' +
                '}';
    }
}
