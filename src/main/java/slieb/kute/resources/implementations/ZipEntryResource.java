package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipEntryResource extends AbstractResource implements Resource.Readable, Resource.InputStreaming {

    private final ZipFile zipFile;

    private final ZipEntry zipEntry;

    public ZipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }




    @Override
    public InputStream getInputStream() throws IOException {
        return zipFile.getInputStream(zipEntry);
    }

    @Override
    public String getPath() {
        return zipEntry.getName();
    }
}
