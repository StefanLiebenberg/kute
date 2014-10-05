package slieb.kute.resources.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecursiveDirectoryIterable implements Iterable<File> {

    private final File directory;

    public RecursiveDirectoryIterable(File directory) {
        this.directory = directory;
    }

    @Override
    public Iterator<File> iterator() {
        return findFiles().iterator();
    }

    private Iterable<File> findFiles() {
        List<File> files = new ArrayList<>();
        findFiles(files, directory);
        return files;
    }


    private void findFiles(List<File> files, File file) {
        if (file != null) {
            for (File child : file.listFiles()) {
                if (child.isDirectory()) {
                    findFiles(files, child);
                } else {
                    files.add(child);
                }
            }
        }
    }
}

