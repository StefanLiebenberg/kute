package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilteredResourceProvider<A extends Resource> implements ResourceProvider<A> {

    private final ResourceProvider<A> resourceProvider;

    private final Predicate<? super Resource> resourceFilter;

    public FilteredResourceProvider(ResourceProvider<A> resourceProvider, Predicate<? super Resource> resourceFilter) {
        this.resourceProvider = resourceProvider;
        this.resourceFilter = resourceFilter;
    }

    @Override
    public Optional<A> getResourceByName(String path) {
        return resourceProvider.getResourceByName(path).filter(resourceFilter::test);
    }

    @Override
    public Stream<A> stream() {
        return resourceProvider.stream().filter(resourceFilter::test);
    }
}
