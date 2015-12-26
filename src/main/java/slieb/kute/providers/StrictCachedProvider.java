package slieb.kute.providers;

import slieb.kute.api.Resource;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


public class StrictCachedProvider implements Resource.Provider {

    private final ConcurrentHashMap<String, Resource.Readable> resourceMap = new ConcurrentHashMap<>();

    private final Resource.Provider resourceProvider;

    private boolean hasBuilt = false;

    public StrictCachedProvider(Resource.Provider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        if (!hasBuilt && !resourceMap.containsKey(path)) {
            Optional<Resource.Readable> optionalResource = resourceProvider.getResourceByName(path);
            if (optionalResource.isPresent()) {
                resourceMap.put(path, optionalResource.get());
            } else {
                resourceMap.remove(path);
            }
        }
        return Optional.ofNullable(resourceMap.getOrDefault(path, null));
    }

    @Override
    public Stream<Resource.Readable> stream() {
        if (!hasBuilt) {
            resourceMap.clear();
            for (Resource.Readable resource : resourceProvider) {
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