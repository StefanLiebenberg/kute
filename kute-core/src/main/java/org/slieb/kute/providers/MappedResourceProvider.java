package org.slieb.kute.providers;

import org.slieb.throwables.FunctionWithThrowable;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.slieb.kute.Kute.distinctPath;

public final class MappedResourceProvider implements Resource.Provider {

    private final Resource.Provider provider;
    private final FunctionWithThrowable<Resource.Readable, Resource.Readable, IOException> function;

    public MappedResourceProvider(final Resource.Provider provider,
                                  final FunctionWithThrowable<Resource.Readable, Resource.Readable, IOException> function) {
        this.provider = provider;
        this.function = function;
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return distinctPath(provider.stream().map(function));
    }
}
