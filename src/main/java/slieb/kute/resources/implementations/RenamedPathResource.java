package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

public class RenamedPathResource<A extends Resource> implements Resource.Proxy<A> {

    private final A resource;

    private final String path;

    public RenamedPathResource(A resource, String path) {
        this.resource = resource;
        this.path = path;
    }

    @Override
    public A getResource() {
        return resource;
    }

    @Override
    public String getPath() {
        return path;
    }
    
}
