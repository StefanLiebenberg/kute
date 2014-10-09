package slieb.kute.resources.providers;

import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.Resources;
import slieb.kute.resources.implementations.FileResource;
import slieb.kute.utilities.fs.DirectoryIterable;

import java.io.File;
import java.util.Iterator;

public class FileResourceProvider implements ResourceProvider<FileResource> {

    public final File directory;

    public FileResourceProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public FileResource getResourceByName(String path) {
        File file = new File(directory, path);
        if (file.exists()) {
            return Resources.fileResource(file, path);
        } else {
            return null;
        }
    }

    @Override
    public Iterable<FileResource> getResources() {
        return new FileResourceIteratable(new DirectoryIterable(directory), directory.getPath());
    }


    private static class FileResourceIteratable implements Iterable<FileResource> {

        private final Iterable<File> fileIterable;

        private final String rootPath;

        private FileResourceIteratable(Iterable<File> fileIterable, String rootPath) {
            this.fileIterable = fileIterable;
            this.rootPath = rootPath;
        }

        @Override
        public Iterator<FileResource> iterator() {
            return new FileResourceIterator(fileIterable.iterator(), rootPath);
        }
    }

    private static class FileResourceIterator implements Iterator<FileResource> {

        private final Iterator<File> fileIterator;

        private final String rootPath;

        private FileResourceIterator(Iterator<File> fileIterator, String rootPath) {
            this.fileIterator = fileIterator;
            this.rootPath = rootPath;
        }

        @Override
        public boolean hasNext() {
            return fileIterator.hasNext();
        }

        @Override
        public FileResource next() {
            File file = fileIterator.next();
            String path = file.getPath();
            String newPath = path.replaceFirst(rootPath, "");
            return Resources.fileResource(file, newPath);
        }

        @Override
        public void remove() {
            fileIterator.remove();
        }
    }
}
