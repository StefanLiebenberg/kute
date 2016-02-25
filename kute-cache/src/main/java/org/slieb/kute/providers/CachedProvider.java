package org.slieb.kute.providers;

import org.slieb.kute.resources.CachedResource;
import org.slieb.kute.api.Resource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.slieb.throwables.FunctionWithThrowable.castFunctionWithThrowable;

/**
 * Caches all resources in provider very eagerly.
 */
public class CachedProvider implements Resource.Provider {

    private final List<Resource.Readable> cachedResourceList;

    public CachedProvider(final Resource.Provider provider) {
        cachedResourceList = provider.stream().map(castFunctionWithThrowable(CachedResource::new)).collect(toList());
    }

    public Stream<Resource.Readable> stream() {
        return cachedResourceList.stream();
    }
}
