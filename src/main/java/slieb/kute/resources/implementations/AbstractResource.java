package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.Serializable;


public abstract class AbstractResource implements Resource, Serializable {

    private final String path;

    public AbstractResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractResource)) return false;
        AbstractResource that = (AbstractResource) o;
        return getPath().equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

}
