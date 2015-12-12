package slieb.kute.resources.providers;

import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileResourceProvider implements ResourceProvider<Resource.InputStreaming> {

    private final ZipFile zipFile;

    public ZipFileResourceProvider(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public Optional<Resource.InputStreaming> getResourceByName(String path) {
        if (path.startsWith("/")) {
            String entryName = path.substring(1);
            return entryStream().filter(e -> entryName.equals(e.getName())).map(this::getZipResource).findFirst();
        }
        return Optional.empty();
    }

    @Override
    public Stream<Resource.InputStreaming> stream() {
        return entryStream().map(this::getZipResource);
    }

    private Stream<? extends ZipEntry> entryStream() {
        return zipFile.stream().filter(e -> !e.isDirectory());
    }

    private Resource.InputStreaming getZipResource(ZipEntry zipEntry) {
        return Kute.zipEntryResource("/" + zipEntry.getName(), zipFile, zipEntry);
    }

}





