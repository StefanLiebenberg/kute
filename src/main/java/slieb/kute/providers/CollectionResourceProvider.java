package slieb.kute.providers;

import slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static slieb.kute.Kute.distinctPath;
import static slieb.kute.Kute.findResource;


public class CollectionResourceProvider implements Resource.Provider, Serializable {

    private final Collection<Resource.Readable> resources;

    public CollectionResourceProvider(Collection<Resource.Readable> resources) {
        this.resources = resources;
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        return findResource(stream(), path);
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return distinctPath(resources.stream());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionResourceProvider)) return false;
        CollectionResourceProvider readables = (CollectionResourceProvider) o;
        return Objects.equals(resources, readables.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resources);
    }

    @Override
    public String toString() {
        return "CollectionResourceProvider{" +
                "resources=" + resources +
                '}';
    }
}
