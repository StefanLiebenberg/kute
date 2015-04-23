package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Collection;
import java.util.stream.Stream;

public class GroupResourceProvider<A extends Resource> implements ResourceProvider<A> {

    private final Collection<ResourceProvider<? extends A>> resourceProviders;

    public GroupResourceProvider(Collection<ResourceProvider<? extends A>> resourceProviders) {
        this.resourceProviders = resourceProviders;
    }

    @Override
    public A getResourceByName(String path) {
        for (ResourceProvider<? extends A> resourceProvider : resourceProviders) {
            A resource = resourceProvider.getResourceByName(path);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public Stream<A> stream() {
        return ProviderUtils.distinct(resourceProviders.stream().flatMap(ResourceProvider::stream));
    }

}

