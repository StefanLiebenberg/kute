package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Collection;
import java.util.stream.Stream;

import static slieb.kute.resources.Resources.distinct;
import static slieb.kute.resources.Resources.findFirst;

public class GroupResourceProvider<A extends Resource> implements ResourceProvider<A> {

    private final Collection<ResourceProvider<? extends A>> resourceProviders;

    public GroupResourceProvider(Collection<ResourceProvider<? extends A>> resourceProviders) {
        this.resourceProviders = resourceProviders;
    }

    @Override
    public A getResourceByName(String path) {
        return findFirst(resourceProviders.stream().map(p -> p.getResourceByName(path)));
    }

    @Override
    public Stream<A> stream() {
        return distinct(resourceProviders.stream().flatMap(ResourceProvider::stream));
    }

}

