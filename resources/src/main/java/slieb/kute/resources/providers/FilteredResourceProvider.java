package slieb.kute.resources.providers;

import slieb.kute.resources.Resource;
import slieb.kute.resources.ResourceFilter;
import slieb.kute.resources.ResourceProvider;

import java.util.Iterator;

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
    public Iterable<A> getResources() {
        return new FilteredIterable<A>(this);
    }

    private static class FilteredIterable<A extends Resource> implements Iterable<A> {

        private final FilteredResourceProvider<A> filteredResourceProvider;

        private FilteredIterable(FilteredResourceProvider<A> filteredResourceProvider) {
            this.filteredResourceProvider = filteredResourceProvider;
        }

        @Override
        public Iterator<A> iterator() {
            return new FilteredIterator<A>(filteredResourceProvider.resourceProvider.getResources().iterator(), filteredResourceProvider.resourceFilter);
        }
    }

    private static class FilteredIterator<A extends Resource> implements Iterator<A> {


        private final Iterator<A> iterator;
        private final ResourceFilter filter;
        private A cachedNext;


        private FilteredIterator(Iterator<A> iterator, ResourceFilter filter) {
            this.iterator = iterator;
            this.filter = filter;
        }

        @Override
        public boolean hasNext() {
            if (cachedNext != null) {
                return true;
            }

            while (iterator.hasNext()) {
                A candidate = iterator.next();
                if (filter.accepts(candidate)) {
                    cachedNext = candidate;
                    return true;
                }
            }

            return false;
        }

        @Override
        public A next() {
            if (hasNext()) {
                A value = cachedNext;
                cachedNext = null;
                return value;
            } else {
                return null;
            }
        }

        @Override
        public void remove() {
            iterator.remove();
        }


        private void cycleNext() {

            if (cachedNext == null) {
                while (iterator.hasNext()) {
                    A nextCandidate = iterator.next();
                    if (filter.accepts(nextCandidate)) {
                        cachedNext = nextCandidate;
                        return;
                    }
                }
            }
        }
    }
}
