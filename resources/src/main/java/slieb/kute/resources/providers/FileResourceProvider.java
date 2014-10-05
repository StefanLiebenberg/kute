package slieb.kute.resources.providers;

import slieb.kute.resources.ResourceProvider;
import slieb.kute.resources.api.FileResource;
import slieb.kute.resources.internal.RecursiveDirectoryIterable;

import java.io.File;
import java.util.Iterator;

public class FileResourceProvider implements ResourceProvider<FileResource>, Iterable<FileResource> {

    public final File directory;

    public FileResourceProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public FileResource getResourceByNamespace(String namespace) {
        return new FileResource(new File(directory, namespace));
    }

    @Override
    public Iterable<FileResource> getResources() {
        return this;
    }

    @Override
    public Iterator<FileResource> iterator() {
        return new FileResourceIterator(new RecursiveDirectoryIterable(directory).iterator());
    }

    private class FileResourceIterator implements Iterator<FileResource> {

        private final Iterator<File> fileIterator;

        private FileResourceIterator(Iterator<File> fileIterator) {
            this.fileIterator = fileIterator;
        }

        @Override
        public boolean hasNext() {
            return fileIterator.hasNext();
        }

        @Override
        public FileResource next() {
            final File file = fileIterator.next();
            return new FileResource(file, file.getPath());
        }

        @Override
        public void remove() {
            fileIterator.remove();
        }
    }
}
