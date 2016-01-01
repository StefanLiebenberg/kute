package slieb.kute.providers;


import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.utils.KuteDigest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static slieb.kute.utils.KuteLambdas.unsafeMap;

public final class ChecksumCachedMapProvider implements Resource.Provider {

    private final Resource.Provider resourceProvider;

    private final Resource.Checksumable checksumable;

    private List<Resource.Readable> cachedResources;

    private byte[] cachedChecksum;

    public ChecksumCachedMapProvider(final Resource.Provider resourceProvider,
                                     final Resource.Checksumable checksumable) {
        this.resourceProvider = resourceProvider;
        this.checksumable = checksumable;
    }


    @Override
    public Stream<Resource.Readable> stream() {
        byte[] checksum = KuteDigest.md5(checksumable);
        if (cachedResources == null || cachedChecksum == null || !Arrays.equals(cachedChecksum, checksum)) {
            cachedResources = streamInternal().collect(toList());
            cachedChecksum = checksum;
        }
        return cachedResources.stream();
    }

    private Stream<Resource.Readable> streamInternal() {
        return resourceProvider.stream().map(unsafeMap(Kute::immutableMemoryResource));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChecksumCachedMapProvider)) return false;
        ChecksumCachedMapProvider readables = (ChecksumCachedMapProvider) o;
        return Objects.equals(resourceProvider, readables.resourceProvider) &&
                Objects.equals(checksumable, readables.checksumable) &&
                Objects.equals(cachedResources, readables.cachedResources) &&
                Arrays.equals(cachedChecksum, readables.cachedChecksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceProvider, checksumable, cachedResources, cachedChecksum);
    }

    @Override
    public String toString() {
        return "ChecksumCachedMapProvider{" +
                "resourceProvider=" + resourceProvider +
                ", checksumable=" + checksumable +
                ", cachedResources=" + cachedResources +
                ", cachedChecksum=" + Arrays.toString(cachedChecksum) +
                '}';
    }
}
