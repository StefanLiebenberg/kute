package slieb.kute.resources.providers;

import slieb.kute.resources.Resource;
import slieb.kute.resources.ResourceProvider;
import slieb.kute.resources.api.RenamedPathResource;

import java.util.Iterator;

public class RenamedNamespaceProvider<A extends Resource> implements ResourceProvider<RenamedPathResource<A>>, Iterable<RenamedPathResource<A>> {

    private final ResourceProvider<A> resourceProvider;

    private final String originalRoot;

    private final String renamedRoot;

    public RenamedNamespaceProvider(ResourceProvider<A> resourceProvider, String originalRoot, String renamedRoot) {
        this.resourceProvider = resourceProvider;
        this.originalRoot = originalRoot;
        this.renamedRoot = renamedRoot;
    }

    @Override
    public RenamedPathResource<A> getResourceByName(String path) {
        String origPath = getRenamedPath(path, renamedRoot, originalRoot);
        A origResource = resourceProvider.getResourceByName(origPath);
        return new RenamedPathResource<>(origResource, path);
    }

    @Override
    public Iterable<RenamedPathResource<A>> getResources() {
        return this;
    }

    @Override
    public Iterator<RenamedPathResource<A>> iterator() {
        return new RenamedNamespaceProvider.RenamedPathResourceIterator<>(resourceProvider.getResources().iterator(), originalRoot, renamedRoot);
    }


    private static class RenamedPathResourceIterator<A extends Resource> implements Iterator<RenamedPathResource<A>> {

        private final Iterator<A> iterator;

        private final String originalRoot;

        private final String renamedRoot;

        private RenamedPathResourceIterator(Iterator<A> iterator, String originalRoot, String renamedRoot) {
            this.iterator = iterator;
            this.originalRoot = originalRoot;
            this.renamedRoot = renamedRoot;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public RenamedPathResource<A> next() {
            A resource = iterator.next();
            return new RenamedPathResource<>(resource, getRenamedPath(resource.getPath(), originalRoot, renamedRoot));
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    private static String getRenamedPath(String path, String origPath, String newPath) {
        return path.replaceFirst(origPath, newPath);
    }
}
