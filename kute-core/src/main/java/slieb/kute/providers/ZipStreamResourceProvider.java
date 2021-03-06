package slieb.kute.providers;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.slieb.throwables.SupplierWithThrowable;
import slieb.kute.Kute;
import slieb.kute.api.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipStreamResourceProvider implements Resource.Provider {

    private final SupplierWithThrowable<ZipInputStream, IOException> zipInputStreamSupplier;

    public ZipStreamResourceProvider(SupplierWithThrowable<ZipInputStream, IOException> zipInputStreamSupplier) {
        this.zipInputStreamSupplier = zipInputStreamSupplier;
    }

    @Override
    public Stream<Resource.Readable> stream() {
        try {
            try (ZipInputStream zipStream = zipInputStreamSupplier.getWithThrowable()) {
                Stream.Builder<Resource.Readable> builder = Stream.builder();
                for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
                    if (!entry.isDirectory()) {
                        InputStream inputStream = new BufferedInputStream(ByteStreams.limit(zipStream, entry.getSize()));
                        builder.add(Kute.resourceWithBytes("/" + entry.getName(), IOUtils.toByteArray(inputStream)));
                    }
                }
                return builder.build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZipStreamResourceProvider)) return false;
        ZipStreamResourceProvider readables = (ZipStreamResourceProvider) o;
        return Objects.equals(zipInputStreamSupplier, readables.zipInputStreamSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipInputStreamSupplier);
    }

    @Override
    public String toString() {
        return "ZipStreamResourceProvider{" +
                "zipInputStreamSupplier=" + zipInputStreamSupplier +
                '}';
    }
}
