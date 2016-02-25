package org.slieb.kute.providers;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.slieb.throwables.SupplierWithThrowable;
import org.slieb.kute.Kute;
import org.slieb.kute.api.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Resource Provider that provides from some ZipInputStream supplier
 */
public class ZipStreamResourceProvider implements Resource.Provider {

    private final SupplierWithThrowable<ZipInputStream, IOException> zipInputStreamSupplier;

    /**
     * @param zipInputStreamSupplier The zip Input Stream supplier
     */
    public ZipStreamResourceProvider(SupplierWithThrowable<ZipInputStream, IOException> zipInputStreamSupplier) {
        this.zipInputStreamSupplier = zipInputStreamSupplier;
    }

    @Override
    public Stream<Resource.Readable> stream() {
        try (ZipInputStream zipStream = zipInputStreamSupplier.getWithThrowable()) {
            return Kute.distinctPath(getResourcesFromZipInputStream(zipStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Resource.Readable> getResourcesFromZipInputStream(final ZipInputStream zipStream) throws IOException {
        final Stream.Builder<Resource.Readable> builder = Stream.builder();
        for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
            if (!entry.isDirectory()) {
                InputStream inputStream = new BufferedInputStream(ByteStreams.limit(zipStream, entry.getSize()));
                builder.add(Kute.resourceWithBytes("/" + entry.getName(), IOUtils.toByteArray(inputStream)));
            }
        }
        return builder.build();
    }
}
