package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

public class RenamedPathResource<A extends Resource> extends AbstractProxy<A> {
    public RenamedPathResource(String path, A resource) {
        super(path, resource);
    }
}
