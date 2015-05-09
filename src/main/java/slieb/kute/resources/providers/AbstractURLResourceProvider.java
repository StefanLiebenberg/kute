package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.ResourceProviderFactory;
import slieb.kute.resources.Resources;

import java.net.URL;
import java.util.stream.Stream;


public abstract class AbstractURLResourceProvider implements ResourceProvider<Resource.InputStreaming> {

    @Override
    public Resource.InputStreaming getResourceByName(String path) {
        return Resources.findFirstResource(providerStream().map(s -> s.getResourceByName(path)));
    }

    @Override
    public Stream<Resource.InputStreaming> stream() {
        return providerStream().flatMap(ResourceProvider::stream);
    }

    public Stream<ResourceProvider<? extends Resource.InputStreaming>> providerStream() {
        return urlStream().map(ResourceProviderFactory::safeCreate).filter(p -> p != null);
    }

    protected abstract Stream<URL> urlStream();
}

