package slieb.kute.resources;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;


public class ProviderUtils {

    public static <R extends Resource> Stream<R> distinct(Stream<R> stream) {
        return stream.map((Function<R, Wrapper<R>>) Wrapper::new).distinct().map(Wrapper::getResource);
    }

    public static <R extends Resource> Iterable<R> filter(Iterable<R> iterable, ResourceFilter filter) {
        return () -> new FilteredIterator<>(iterable.iterator(), filter);
    }

    public static <R extends Resource> R find(Stream<R> stream, String path) {
        return stream.filter(r -> r.getPath().equals(path)).findFirst().orElse(null);
    }
}

class Wrapper<R extends Resource> implements Serializable {

    final String path;
    final R resource;

    public Wrapper(R resource) {
        this.resource = resource;
        this.path = resource.getPath();
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wrapper)) return false;

        Wrapper<?> map = (Wrapper<?>) o;

        return path.equals(map.path);
    }

    public R getResource() {
        return resource;
    }
}


class FilteredIterator<A extends Resource> implements Iterator<A> {


    private final Iterator<A> iterator;
    private final ResourceFilter filter;
    private A cachedNext;


    public FilteredIterator(Iterator<A> iterator, ResourceFilter filter) {
        this.iterator = iterator;
        this.filter = filter;
    }

    @Override
    public boolean hasNext() {
        if (cachedNext != null) {
            return true;
        }

        while (iterator.hasNext()) {
            A candidate = iterator.next();
            if (filter.accepts(candidate)) {
                cachedNext = candidate;
                return true;
            }
        }

        return false;
    }

    @Override
    public A next() {
        if (hasNext()) {
            A value = cachedNext;
            cachedNext = null;
            return value;
        } else {
            throw new IllegalStateException("iterator empty");
        }
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
