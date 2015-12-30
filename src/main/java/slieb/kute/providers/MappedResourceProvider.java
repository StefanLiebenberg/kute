package slieb.kute.providers;

import slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class MappedResourceProvider implements Resource.Provider, Serializable {

    private final Resource.Provider provider;
    private final Function<Resource.Readable, Resource.Readable> function;

    public MappedResourceProvider(Resource.Provider provider,
                                  Function<Resource.Readable, Resource.Readable> function) {
        this.provider = provider;
        this.function = function;
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        return provider.getResourceByName(path).map(function);
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return provider.stream().map(function);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappedResourceProvider)) return false;
        MappedResourceProvider readables = (MappedResourceProvider) o;
        return Objects.equals(provider, readables.provider) &&
                Objects.equals(function, readables.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, function);
    }

    @Override
    public String toString() {
        return "MappedResourceProvider{" +
                "provider=" + provider +
                ", function=" + function +
                '}';
    }
}