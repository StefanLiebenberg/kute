package org.slieb.kute.service.providers;


import org.slieb.kute.service.resources.IndexResource;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class IndexProvider implements ResourceProvider<IndexResource> {

    protected final Comparator<IndexResource> INDEXED_COMPARATOR = Comparator.comparingInt(node -> node.hasIndexResource() ? -1 : 1);
    protected final Function<Stream<IndexResource>, Optional<IndexResource>> TO_INDEXED_NODE = stream -> stream.sorted(INDEXED_COMPARATOR).findFirst();
    public final ResourceProvider<? extends Resource.Readable> provider;

    public IndexProvider(ResourceProvider<? extends Resource.Readable> provider) {
        this.provider = provider;
    }

    @Override
    public Stream<IndexResource> stream() {
        return getDirectoryNodeStream();
    }

    @Override
    public IndexResource getResourceByName(String path) {
        return getDirectoryNodeStream()
                .filter(node -> node.getPath().equals(path))
                .findFirst().orElse(null);
    }

    protected Stream<IndexResource> getDirectoryNodeStream() {
        return provider.stream()
                .flatMap(this::getDirectoryNodeStreamForResource)
                .collect(groupingBy(Resource::getPath))
                .values().stream()
                .map(Collection::stream)
                .map(TO_INDEXED_NODE)
                .filter(Optional::isPresent).map(Optional::get);
    }

    protected Stream<IndexResource> getDirectoryNodeStreamForResource(Resource.Readable resource) {
        Stream.Builder<IndexResource> builder = Stream.builder();
        Path path = Paths.get(resource.getPath());
        Path parent = path.getParent();
        if (path.endsWith("/index.html")) {
            builder.accept(new IndexResource(parent.toString(), this.provider, resource));
            parent = parent.getParent();
        }

        for (Path current = parent; current != null; current = current.getParent()) {
            builder.accept(new IndexResource(current.toString(), this.provider, null));
        }
        return builder.build();
    }
}