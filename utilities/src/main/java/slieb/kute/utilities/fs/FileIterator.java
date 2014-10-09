package slieb.kute.utilities.fs;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;


public class FileIterator implements Iterator<File> {

    private final boolean includeDirectories;

    private final boolean recursive;

    private Stack<Iterator<File>> stack = new Stack<>();

    private Iterator<File> currentIterator;

    private File cachedNextFile;

    private boolean hasNextFileCached = false;


    public FileIterator(File directory, boolean recursive, boolean includeDirectories) {

        this.recursive = recursive;
        this.includeDirectories = includeDirectories;

        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            currentIterator = Arrays.asList(listFiles).iterator();
        }

    }


    @Override
    public boolean hasNext() {
        if (hasNextFileCached) {
            return true;
        }

        while (currentIterator != null) {
            if (!currentIterator.hasNext()) {
                currentIterator = stack.isEmpty() ? null : stack.pop();
            } else {
                File file = currentIterator.next();
                if (file.isDirectory()) {
                    if (recursive) {
                        File[] files = file.listFiles();
                        if (files != null) {
                            stack.push(currentIterator);
                            currentIterator = Arrays.asList(files).iterator();
                        }
                    }
                    if (includeDirectories) {
                        cachedNextFile = file;
                        hasNextFileCached = true;
                        return true;
                    }
                } else {
                    hasNextFileCached = true;
                    cachedNextFile = file;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public File next() {
        if (hasNextFileCached) {
            hasNextFileCached = false;
            return cachedNextFile;
        }

        while (currentIterator != null) {
            if (!currentIterator.hasNext()) {
                if (!stack.isEmpty()) {
                    throw createBadNextCall();
                }
                currentIterator = stack.pop();
            } else {
                File file = currentIterator.next();
                if (file.isDirectory()) {
                    if (recursive) {
                        File[] files = file.listFiles();
                        if (files != null) {
                            stack.push(currentIterator);
                            currentIterator = Arrays.asList(files).iterator();
                        }
                    }
                    if (includeDirectories) {
                        return file;
                    }
                } else {
                    return file;
                }
            }
        }

        throw createBadNextCall();
    }

    @Override
    public void remove() {
        currentIterator.remove();
    }


    private IllegalStateException createBadNextCall() {
        return new IllegalStateException("Cannot call next on empty iterator.");
    }


}
