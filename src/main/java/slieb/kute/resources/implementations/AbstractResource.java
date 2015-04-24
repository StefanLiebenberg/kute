package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.Serializable;


public abstract class AbstractResource implements Resource, Serializable {

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

    @Override
    public String toString() {
        return String.format("[%s:%s]", getClass().getName(), getPath());
    }
}
