package slieb.kute.providers;

import org.slieb.unnamed.api.FunctionWithException;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

public final class MappedResourceProvider implements Resource.Provider, Serializable {

    private final Resource.Provider provider;
    private final FunctionWithException<Resource.Readable, Resource.Readable, IOException> function;

    public MappedResourceProvider(final Resource.Provider provider,
                                  final FunctionWithException<Resource.Readable, Resource.Readable, IOException> function) {
        this.provider = provider;
        this.function = function;
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
