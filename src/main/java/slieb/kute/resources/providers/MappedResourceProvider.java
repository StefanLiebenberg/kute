package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Iterator;
import java.util.function.Function;


public class MappedResourceProvider<A extends Resource, B extends Resource> implements ResourceProvider<B> {

    private final ResourceProvider<A> provider;
    private final Function<A, B> function;

    public MappedResourceProvider(ResourceProvider<A> provider, Function<A, B> function) {
        this.provider = provider;
        this.function = function;
    }

    @Override
    public B getResourceByName(String path) {
        return null;
    }

    @Override
    public Iterable<B> getResources() {
        return new MappedResourceIterable<A, B>(provider.getResources(), function);
    }

}

class MappedResourceIterable<A extends Resource, B extends Resource> implements Iterable<B> {

    private final Iterable<A> resourceIterable;

    private final Function<A, B> function;

    public MappedResourceIterable(Iterable<A> resourceIterable, Function<A, B> function) {
        this.resourceIterable = resourceIterable;
        this.function = function;
    }

    @Override
    public Iterator<B> iterator() {
        return new MappedResourceIterator<>(resourceIterable.iterator(), function);
    }
}

class MappedResourceIterator<A extends Resource, B extends Resource> implements Iterator<B> {

    private final Iterator<A> iterator;

    private final Function<A, B> function;

    public MappedResourceIterator(Iterator<A> iterator, Function<A, B> function) {
        this.iterator = iterator;
        this.function = function;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public B next() {
        return function.apply(iterator.next());
    }
}