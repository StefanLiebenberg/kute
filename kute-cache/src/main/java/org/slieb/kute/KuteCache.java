package org.slieb.kute;

import org.slieb.kute.providers.CachedProvider;
import org.slieb.kute.providers.ChecksumCachedMappingProvider;
import org.slieb.kute.providers.ChecksumCachedProvider;
import org.slieb.kute.resources.CachedResource;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public class KuteCache {

    public static CachedResource cachedResource(Resource.Readable readable) throws IOException {
        return new CachedResource(readable);
    }

    public static CachedProvider cachedProvider(Resource.Provider provider) {
        return new CachedProvider(provider);
    }

    public static ChecksumCachedMappingProvider checksumCacheMapping(final Resource.Provider source,
                                                                     final Function<Resource.Provider, Resource.Provider> function) {
        return new ChecksumCachedMappingProvider(source, function);
    }

    public static ChecksumCachedProvider checksumCachedProvider(final Resource.Checksumable checksumable,
                                                                final Supplier<Resource.Provider> providerSupplier) {
        return new ChecksumCachedProvider(checksumable, providerSupplier);
    }
}
