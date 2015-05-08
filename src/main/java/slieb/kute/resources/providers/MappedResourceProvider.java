package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.function.Function;
import java.util.stream.Stream;

public class MappedResourceProvider<A extends Resource, B extends Resource> implements ResourceProvider<B> {

    private final ResourceProvider<A> provider;
    private final Function<A, B> function;

    public MappedResourceProvider(ResourceProvider<A> provider, Function<A, B> function) {
        this.provider = provider;
        this.function = function;
    }

    @Override
    public B getResourceByName(String path) {
        A resource = provider.getResourceByName(path);
        if (resource != null) {
            return function.apply(resource);
        } else {
            return null;
        }
    }

    @Override
    public Stream<B> stream() {
        return provider.stream().map(function);
    }

}
