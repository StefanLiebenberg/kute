package slieb.kute.providers;

import slieb.kute.api.Resource;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;


public class CollectionProvider implements Resource.Provider {

    private final Collection<Resource.Readable> collection;

    public CollectionProvider(Collection<Resource.Readable> collection) {
        this.collection = collection;
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return collection.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionProvider)) return false;
        CollectionProvider readables = (CollectionProvider) o;
        return Objects.equals(collection, readables.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection);
    }

    @Override
    public String toString() {
        return "CollectionProvider{" +
                "collection=" + collection +
                '}';
    }
}
