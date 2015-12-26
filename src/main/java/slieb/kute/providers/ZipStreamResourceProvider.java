package slieb.kute.providers;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import slieb.kute.Kute;
import slieb.kute.api.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipStreamResourceProvider implements Resource.Provider {

    private final Supplier<ZipInputStream> zipInputStreamSupplier;

    public ZipStreamResourceProvider(Supplier<ZipInputStream> zipInputStreamSupplier) {
        this.zipInputStreamSupplier = zipInputStreamSupplier;
    }

    @Override
    public Stream<Resource.Readable> stream() {
        try {
            try (ZipInputStream zipStream = zipInputStreamSupplier.get()) {
                Stream.Builder<Resource.Readable> builder = Stream.builder();
                for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
                    if (!entry.isDirectory()) {
                        InputStream inputStream = new BufferedInputStream(
                                ByteStreams.limit(zipStream, entry.getSize()));
                        builder.add(Kute.resourceWithBytes("/" + entry.getName(), IOUtils.toByteArray(inputStream)));
                    }
                }
                return builder.build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
