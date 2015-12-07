package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Optional;
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
    public Optional<A> getResourceByName(String path) {
        if (!hasBuilt && !resourceMap.containsKey(path)) {
            Optional<A> optionalResource = resourceProvider.getResourceByName(path);
            if (optionalResource.isPresent()) {
                resourceMap.put(path, optionalResource.get());
            } else {
                resourceMap.remove(path);
            }
        }
        return Optional.ofNullable(resourceMap.getOrDefault(path, null));
    }

    @Override
    public Stream<A> stream() {
        if (!hasBuilt) {
            resourceMap.clear();
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