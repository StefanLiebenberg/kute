package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class StrictCachedProvider<A extends Resource.Writeable> implements ResourceProvider<A> {

    private final ConcurrentHashMap<String, A> resourceMap = new ConcurrentHashMap<>();

    private final ResourceProvider<A> resourceProvider;

    private boolean hasBuilt = false;

    public StrictCachedProvider(ResourceProvider<A> resourceProvider) {
        this.resourceProvider = resourceProvider;
    }


    @Override
    public A getResourceByName(String path) {
        if (!hasBuilt && !resourceMap.containsKey(path)) {
            resourceMap.put(path, resourceProvider.getResourceByName(path));
        }
        return resourceMap.get(path);
    }

    @Override
    public Stream<A> stream() {
        if (!hasBuilt) {
            for (A resource : resourceProvider) {
                resourceMap.put(resource.getPath(), resource);
            }
        }
        return resourceMap.values().stream();
    }


    public void reset() {
        hasBuilt = false;
        resourceMap.clear();
    }

}