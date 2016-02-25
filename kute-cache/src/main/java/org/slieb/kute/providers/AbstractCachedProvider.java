package org.slieb.kute.providers;

import org.slieb.kute.KuteCache;
import org.slieb.kute.api.Resource;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractCachedProvider implements Resource.Provider {

    private Resource.Provider cachedProvider;
    private byte[] cachedChecksum;

    protected abstract byte[] getChecksum();

    protected abstract Resource.Provider getProvider();

    private Resource.Provider getCachedProvider() {
        final byte[] checksum = getChecksum();
        if (cachedProvider == null || (cachedChecksum != null && !Arrays.equals(checksum, cachedChecksum))) {
            cachedProvider = KuteCache.cachedProvider(getProvider());
            cachedChecksum = checksum;
        }
        return cachedProvider;
    }

    public synchronized void invalidate() {
        cachedProvider = null;
        cachedChecksum = null;
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return getCachedProvider().stream();
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(final String path) {
        return getCachedProvider().getResourceByName(path);
    }
}
