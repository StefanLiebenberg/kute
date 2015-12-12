package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static slieb.kute.Kute.distinctPath;
import static slieb.kute.Kute.findResource;


public class CollectionResourceProvider<R extends Resource> implements ResourceProvider<R> {

    private final Collection<R> resources;

    public CollectionResourceProvider(Collection<R> resources) {
        this.resources = resources;
    }

    @Override
    public Optional<R> getResourceByName(String path) {
        return findResource(stream(), path);
    }

    @Override
    public Stream<R> stream() {
        return distinctPath(resources.stream());
    }

}
