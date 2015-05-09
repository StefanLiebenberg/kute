package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.Resources;

import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileResourceProvider implements ResourceProvider<Resource.InputStreaming> {

    private final ZipFile zipFile;

    public ZipFileResourceProvider(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public Resource.InputStreaming getResourceByName(String path) {
        ZipEntry entry = zipFile.getEntry(path);
        if (entry != null) {
            return getZipResource(entry);
        } else {
            return null;
        }
    }

    @Override
    public Stream<Resource.InputStreaming> stream() {
        return zipFile.stream().map(this::getZipResource);
    }

    private Resource.InputStreaming getZipResource(ZipEntry zipEntry) {
        return Resources.zipEntryResource(zipFile, zipEntry);
    }

}





