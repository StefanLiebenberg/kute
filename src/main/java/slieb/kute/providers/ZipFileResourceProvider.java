package slieb.kute.providers;

import slieb.kute.Kute;
import slieb.kute.api.Resource;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileResourceProvider implements Resource.Provider {

    private final ZipFile zipFile;

    public ZipFileResourceProvider(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        return Optional.of(path).filter(p -> p.startsWith("/")).map(p -> p.substring(1)).flatMap(this::getEntryByName);
    }

    private Optional<Resource.Readable> getEntryByName(final String entryName) {
        return entryStream().filter(e -> entryName.equals(e.getName())).map(this::getZipResource).findFirst();
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return entryStream().map(this::getZipResource);
    }

    private Stream<? extends ZipEntry> entryStream() {
        return zipFile.stream().filter(e -> !e.isDirectory());
    }

    private Resource.Readable getZipResource(ZipEntry zipEntry) {
        return Kute.zipEntryResource("/" + zipEntry.getName(), zipFile, zipEntry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZipFileResourceProvider)) return false;
        ZipFileResourceProvider readables = (ZipFileResourceProvider) o;
        return Objects.equals(zipFile, readables.zipFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipFile);
    }

    @Override
    public String toString() {
        return "ZipFileResourceProvider{" +
                "zipFile=" + zipFile +
                '}';
    }
}





