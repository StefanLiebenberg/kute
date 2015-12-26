package slieb.kute.providers;


import slieb.kute.api.Resource;
import slieb.kute.resources.MutableResource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ConcurentMapResourceProvider implements Resource.Provider, Resource.Creator {

    private final ConcurrentHashMap<String, MutableResource> concurrentHashMap;

    public ConcurentMapResourceProvider() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Resource.Writable create(String path) {
        if (!concurrentHashMap.containsKey(path)) {
            concurrentHashMap.put(path, new MutableResource(path));
        }
        return concurrentHashMap.get(path);
    }

    @Override
    public synchronized Stream<Resource.Readable> stream() {
        return concurrentHashMap.values().stream().map(r -> r);
    }

    public synchronized void clear() {
        concurrentHashMap.clear();
    }
}
