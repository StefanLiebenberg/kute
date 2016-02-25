package org.slieb.kute.providers;

import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

public class ChecksumCachedProvider extends AbstractCachedProvider {

    private final Resource.Checksumable checksumable;

    private final Supplier<Resource.Provider> providerSupplier;

    public ChecksumCachedProvider(final Resource.Checksumable checksumable,
                                  final Supplier<Resource.Provider> providerSupplier) {
        this.checksumable = checksumable;
        this.providerSupplier = providerSupplier;
    }

    @Override
    protected byte[] getChecksum() {
        try {
            return checksumable.checksum("MD5");
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Resource.Provider getProvider() {
        return providerSupplier.get();
    }
}
