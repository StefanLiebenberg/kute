package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

public class RenamedPathResource<A extends Resource> extends AbstractProxy<A> {

    private final String path;

    public RenamedPathResource(A resource, String path) {
        super(resource);
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }


}
