package slieb.kute.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourcePredicate;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class FilteredResourceProvider implements Resource.Provider {

    private final Resource.Provider resourceProvider;

    private final ResourcePredicate<? super Resource> resourceFilter;

    public FilteredResourceProvider(final Resource.Provider resourceProvider,
                                    final ResourcePredicate<? super Resource> resourceFilter) {
        this.resourceProvider = resourceProvider;
        this.resourceFilter = resourceFilter;
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        return resourceProvider.getResourceByName(path).filter(resourceFilter);
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return resourceProvider.stream().filter(resourceFilter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilteredResourceProvider)) return false;
        FilteredResourceProvider readables = (FilteredResourceProvider) o;
        return Objects.equals(resourceProvider, readables.resourceProvider) &&
                Objects.equals(resourceFilter, readables.resourceFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceProvider, resourceFilter);
    }

    @Override
    public String toString() {
        return "FilteredResourceProvider{" +
                "resourceProvider=" + resourceProvider +
                ", resourceFilter=" + resourceFilter +
                '}';
    }
}
