package slieb.kute.resources.providers;

import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.ResourceException;
import slieb.kute.resources.implementations.ZipEntryResource;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileResourceProvider implements ResourceProvider<ZipEntryResource> {

    private final ZipFile zipFile;

    public ZipFileResourceProvider(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public Iterator<ZipEntryResource> iterator() {
        return new ZipEntryIterator(zipFile);
    }

    @Override
    public ZipEntryResource getResourceByName(String path) {
        return getResource(zipFile, zipFile.getEntry(path));
    }

    @Override
    public Stream<ZipEntryResource> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), true);
    }

    public static class ZipEntryIterator implements Iterator<ZipEntryResource> {
        private final ZipFile zipFile;

        private final Enumeration<? extends ZipEntry> enumeration;

        private ZipEntryIterator(ZipFile zipFile) {
            this.zipFile = zipFile;
            this.enumeration = zipFile.entries();
        }

        @Override
        public boolean hasNext() {
            return enumeration.hasMoreElements();
        }

        @Override
        public ZipEntryResource next() {
            return getResource(zipFile, enumeration.nextElement());
        }


        @Override
        public void remove() {
            throw new ResourceException("cannot remove from iterator");
        }
    }


    private static ZipEntryResource getResource(ZipFile zipFile, ZipEntry zipEntry) {
        if (zipEntry != null) {
            return new ZipEntryResource(zipFile, zipEntry);
        } else {
            return null;
        }
    }
}
