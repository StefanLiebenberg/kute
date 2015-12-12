package slieb.kute.resources.providers;

import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.implementations.RenamedPathResource;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static slieb.kute.Kute.renameResource;

public class RenamedNamespaceProvider<A extends Resource> implements ResourceProvider<RenamedPathResource<A>>,
        Iterable<RenamedPathResource<A>> {

    private final ResourceProvider<A> resourceProvider;

    private final String originalRoot;

    private final String renamedRoot;

    public RenamedNamespaceProvider(ResourceProvider<A> resourceProvider, String originalRoot, String renamedRoot) {
        this.resourceProvider = resourceProvider;
        this.originalRoot = originalRoot;
        this.renamedRoot = renamedRoot;
    }


    @Override
    public Optional<RenamedPathResource<A>> getResourceByName(String path) {
        return resourceProvider.getResourceByName(getRenamedPath(path, renamedRoot, originalRoot)).map(
                resource -> renameResource(path, resource));
    }


    @Override
    public Iterator<RenamedPathResource<A>> iterator() {
        return new RenamedNamespaceProvider.RenamedPathResourceIterator<>(resourceProvider.iterator(), originalRoot,
                                                                          renamedRoot);
    }

    @Override
    public Stream<RenamedPathResource<A>> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), true);
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
            return Kute.renameResource(getRenamedPath(resource.getPath(), originalRoot, renamedRoot), resource);
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
