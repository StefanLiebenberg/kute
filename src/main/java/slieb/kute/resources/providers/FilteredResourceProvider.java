package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;
import slieb.kute.api.ResourceProvider;

import java.util.stream.Stream;

public class FilteredResourceProvider<A extends Resource> implements ResourceProvider<A> {

    private final ResourceProvider<A> resourceProvider;

    private final ResourceFilter resourceFilter;

    public FilteredResourceProvider(ResourceProvider<A> resourceProvider, ResourceFilter resourceFilter) {
        this.resourceProvider = resourceProvider;
        this.resourceFilter = resourceFilter;
    }

    @Override
    public A getResourceByName(String path) {
        A resource = resourceProvider.getResourceByName(path);
        if (resource != null && resourceFilter.accepts(resource)) {
            return resource;
        } else {
            return null;
        }
    }

    @Override
    public Stream<A> stream() {
        return resourceProvider.stream().filter(resourceFilter::accepts);
    }
}
