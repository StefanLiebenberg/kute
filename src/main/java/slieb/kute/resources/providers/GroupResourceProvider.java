package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Iterator;


public class GroupResourceProvider<A extends Resource> implements ResourceProvider<A> {

    private final Iterable<ResourceProvider<A>> resourceProviders;

    public GroupResourceProvider(Iterable<ResourceProvider<A>> resourceProviders) {
        this.resourceProviders = resourceProviders;
    }

    @Override
    public A getResourceByName(String path) {
        for (ResourceProvider<A> resourceProvider : resourceProviders) {
            A resource = resourceProvider.getResourceByName(path);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public GroupedResourceIterable<A> getResources() {
        return new GroupedResourceIterable<>(resourceProviders);
    }

    private static class GroupedResourceIterable<A extends Resource> implements Iterable<A> {

        private final Iterable<ResourceProvider<A>> resourceProviders;

        private GroupedResourceIterable(Iterable<ResourceProvider<A>> resourceProviders) {
            this.resourceProviders = resourceProviders;
        }

        @Override
        public GroupedResourcesIterator<A> iterator() {
            return new GroupedResourcesIterator<>(resourceProviders.iterator());
        }
    }

    private static class GroupedResourcesIterator<A extends Resource> implements Iterator<A> {

        private final Iterator<ResourceProvider<A>> iterators;


        private GroupedResourcesIterator(Iterator<ResourceProvider<A>> iterators) {
            this.iterators = iterators;
        }

        private Iterator<A> currentIterator;

        @Override
        public boolean hasNext() {
            if (currentIterator != null && currentIterator.hasNext()) {
                return true;
            }

            while (iterators.hasNext()) {
                currentIterator = iterators.next().getResources().iterator();
                if (currentIterator.hasNext()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public A next() {
            if (currentIterator != null && currentIterator.hasNext()) {
                return currentIterator.next();
            }

            while (iterators.hasNext()) {
                currentIterator = iterators.next().getResources().iterator();
                if (currentIterator.hasNext()) {
                    return currentIterator.next();
                }
            }

            return null; // consider error here.
        }

        @Override
        public void remove() {
            if (currentIterator != null) {
                currentIterator.remove();
            }
        }
    }
}

