package slieb.kute.providers;


import slieb.kute.api.Resource;
import slieb.kute.resources.MutableResource;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ConcurrentMapResourceProvider implements Resource.Provider, Resource.Creator {

    private final ConcurrentHashMap<String, MutableResource> concurrentHashMap;

    public ConcurrentMapResourceProvider() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    @Override
    public Resource.Writable create(String path) {
        if (!concurrentHashMap.containsKey(path)) {
            concurrentHashMap.put(path, new MutableResource(path));
        }
        return concurrentHashMap.get(path);
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return concurrentHashMap.values().stream().map(r -> r);
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        if (concurrentHashMap.containsKey(path)) {
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
        if (this == o) return true;
        if (!(o instanceof ConcurrentMapResourceProvider)) return false;
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
