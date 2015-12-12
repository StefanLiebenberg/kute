package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static slieb.kute.Kute.distinctPath;
import static slieb.kute.Kute.findFirstOptionalResource;

public class GroupResourceProvider<A extends Resource> implements ResourceProvider<A> {

    private final Collection<ResourceProvider<A>> resourceProviders;

    public GroupResourceProvider(Collection<ResourceProvider<A>> resourceProviders) {
        this.resourceProviders = resourceProviders;
    }

    @Override
    public Optional<A> getResourceByName(String path) {
        return findFirstOptionalResource(resourceProviders.stream().map(p -> p.getResourceByName(path)));
    }

    @Override
    public Stream<A> stream() {
        return distinctPath(resourceProviders.stream().flatMap(ResourceProvider::stream));
    }

}

