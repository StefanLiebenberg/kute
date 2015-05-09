package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.Resources;

import java.util.Collection;
import java.util.stream.Stream;


public class CollectionResourceProvider<R extends Resource> implements ResourceProvider<R> {

    private final Collection<R> resources;

    public CollectionResourceProvider(Collection<R> resources) {
        this.resources = resources;
    }

    @Override
    public R getResourceByName(String path) {
        return Resources.find(stream(), path);
    }

    @Override
    public Stream<R> stream() {
        return Resources.distinct(resources.stream());
    }

}
