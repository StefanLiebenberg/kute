package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.*;

import static slieb.kute.Kute.getResourceAs;


public abstract class AbstractProxy<R extends Resource> extends AbstractResource implements Resource.Proxy<R> {

    private final R resource;

    public AbstractProxy(R resource) {
        this(resource.getPath(), resource);
    }

    public AbstractProxy(String path, R resource) {
        super(path);
        this.resource = resource;
    }

    public R getResource() {
        return resource;
    }

}
