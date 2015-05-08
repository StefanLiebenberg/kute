package slieb.kute.resources.providers;

import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.implementations.ZipEntryResource;

import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileResourceProvider implements ResourceProvider<ZipEntryResource> {

    private final ZipFile zipFile;

    public ZipFileResourceProvider(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public ZipEntryResource getResourceByName(String path) {
        ZipEntry entry = zipFile.getEntry(path);
        if (entry != null) {
            return getZipResource(entry);
        } else {
            return null;
        }
    }

    @Override
    public Stream<ZipEntryResource> stream() {
        return zipFile.stream().map(this::getZipResource);
    }

    private ZipEntryResource getZipResource(ZipEntry zipEntry) {
        return new ZipEntryResource(zipFile, zipEntry);
    }

}





