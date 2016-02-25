package org.slieb.kute.providers;

import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public class ChecksumCachedMappingProvider extends AbstractCachedProvider {

    private final Resource.Provider provider;

    private final Function<Resource.Provider, Resource.Provider> function;

    public ChecksumCachedMappingProvider(final Resource.Provider source,
                                         final Function<Resource.Provider, Resource.Provider> function) {
        this.provider = source;
        this.function = function;
    }

    @Override
    protected byte[] getChecksum() {
        try {
            return provider.checksum("MD5");
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource.Provider getProvider() {
        return function.apply(provider);
    }
}
