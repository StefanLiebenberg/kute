package slieb.kute.resources.providers;

import slieb.kute.resources.ResourceProvider;
import slieb.kute.resources.api.ZipEntryResource;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileResourceProvider implements ResourceProvider<ZipEntryResource> {
    private final ZipFile zipFile;

    public ZipFileResourceProvider(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public ZipEntryResource getResourceByName(String path) {
        return getResource(zipFile, zipFile.getEntry(path));
    }

    @Override
    public ZipEntryIterable getResources() {
        return new ZipEntryIterable(zipFile);
    }

    private static class ZipEntryIterable implements Iterable<ZipEntryResource> {

        private final ZipFile zipFile;

        private ZipEntryIterable(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        @Override
        public Iterator<ZipEntryResource> iterator() {
            return new ZipEntryIterator(zipFile);
        }
    }


    private static class ZipEntryIterator implements Iterator<ZipEntryResource> {
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
            throw new RuntimeException("cannot remove");
        }
    }


    private static ZipEntryResource getResource(ZipFile zipFile, ZipEntry zipEntry) {
        return new ZipEntryResource(zipFile, zipEntry);
    }
}
