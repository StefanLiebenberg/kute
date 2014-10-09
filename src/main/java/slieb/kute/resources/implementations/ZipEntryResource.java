package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipEntryResource implements Resource.Readable {
    private final ZipFile zipFile;
    private final ZipEntry zipEntry;

    public ZipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(zipFile.getInputStream(zipEntry));
    }

    @Override
    public String getPath() {
        return zipEntry.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZipEntryResource)) return false;

        ZipEntryResource resource = (ZipEntryResource) o;

        if (!zipEntry.equals(resource.zipEntry)) return false;
        if (!zipFile.equals(resource.zipFile)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = zipFile.hashCode();
        result = 31 * result + zipEntry.hashCode();
        return result;
    }
}
