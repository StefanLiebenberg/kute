package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.HashMap;

public class StrictCachedProvider<A extends Resource.Writeable> implements ResourceProvider<A> {

    private final HashMap<String, A> resourceMap = new HashMap<>();

    private final ResourceProvider<A> resourceProvider;

    private final boolean fifo;

    public StrictCachedProvider(ResourceProvider<A> resourceProvider) {
        this.resourceProvider = resourceProvider;
        this.fifo = true;
    }

    public StrictCachedProvider(ResourceProvider<A> resourceProvider, boolean fifo) {
        this.resourceProvider = resourceProvider;
        this.fifo = fifo;
    }

    @Override
    public A getResourceByName(String path) {
        if (resourceMap.isEmpty()) {
            build();
        }
        return resourceMap.get(path);
    }

    @Override
    public Iterable<A> getResources() {
        if (resourceMap.isEmpty()) {
            build();
        }
        return resourceMap.values();
    }

    public void reset() {
        resourceMap.clear();
    }

    private void build() {
        for (A resource : resourceProvider.getResources()) {
            String path = resource.getPath();
            if (!fifo || !resourceMap.containsKey(path)) {
                resourceMap.put(path, resource);
            }
        }
    }
}