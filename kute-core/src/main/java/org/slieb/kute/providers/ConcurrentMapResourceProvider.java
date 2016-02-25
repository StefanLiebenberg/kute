package org.slieb.kute.providers;

import org.slieb.kute.resources.MutableBytesArrayResource;
import org.slieb.kute.api.Resource;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;

public class ConcurrentMapResourceProvider implements Resource.Provider, Resource.Creator {

    private final ConcurrentHashMap<String, MutableBytesArrayResource> concurrentHashMap;

    public ConcurrentMapResourceProvider() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    @Override
    public MutableBytesArrayResource create(String path) {
        if (!concurrentHashMap.containsKey(path)) {
            concurrentHashMap.put(path, new MutableBytesArrayResource(path));
        }
        return concurrentHashMap.get(path);
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return concurrentHashMap.values().stream()
                                .sorted(comparing(Resource::getPath))
                                .map(identity());
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        if (path != null && concurrentHashMap.containsKey(path)) {
            return Optional.of(concurrentHashMap.get(path));
        } else {
            return Optional.empty();
        }
    }

    public void clear() {
        concurrentHashMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ConcurrentMapResourceProvider)) { return false; }
        ConcurrentMapResourceProvider readables = (ConcurrentMapResourceProvider) o;
        return Objects.equals(concurrentHashMap, readables.concurrentHashMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concurrentHashMap);
    }

    @Override
    public String toString() {
        return "ConcurrentMapResourceProvider{" +
                "concurrentHashMap=" + concurrentHashMap +
                '}';
    }
}
