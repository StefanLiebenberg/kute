package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.ResourceProviderFactory;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;


public abstract class AbstractURLResourceProvider implements ResourceProvider<Resource.Readable> {

    public Resource.Readable getResourceByName(String path) {
        return ProviderUtils.find(stream(), path);
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return getURLStream()
                .map(this::safeCreate)
                .filter(p -> p != null)
                .flatMap(ResourceProvider::stream);
    }

    private ResourceProvider<? extends Resource.Readable> safeCreate(URL url) {
        try {
            return ResourceProviderFactory.create(url);
        } catch (IOException ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    protected abstract Stream<URL> getURLStream();
}

