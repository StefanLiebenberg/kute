package slieb.kute.utilities.fs;

import java.io.File;
import java.util.Iterator;

public class DirectoryIterable implements Iterable<File> {

    private final File directory;

    private final boolean recursive, includeDirectory;

    public DirectoryIterable(File directory, boolean recursive, boolean includeDirectory) {
        this.directory = directory;
        this.recursive = recursive;
        this.includeDirectory = includeDirectory;
    }

    public DirectoryIterable(File directory) {
        this.directory = directory;
        this.recursive = true;
        this.includeDirectory = false;
    }

    @Override
    public Iterator<File> iterator() {
        return new FileIterator(directory, recursive, includeDirectory);
    }
}
