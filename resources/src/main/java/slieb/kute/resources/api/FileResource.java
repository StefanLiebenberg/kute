package slieb.kute.resources.api;

import slieb.kute.resources.Resource;

import java.io.*;

public class FileResource implements Resource.Writeable, Resource.Readable {

    private final File file;

    private final String path;

    public FileResource(File file) {
        this(file, file.getPath());
    }

    public FileResource(File file, String path) {
        this.file = file;
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public FileReader getReader() throws IOException {
        return new FileReader(this.file);
    }

    @Override
    public FileWriter getWriter() throws IOException {
        return new FileWriter(this.file);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileResource that = (FileResource) o;

        if (!file.equals(that.file)) return false;
        if (!path.equals(that.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = file.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
