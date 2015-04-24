package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileResource
        extends AbstractResource
        implements Resource.Writeable, Resource.Readable {

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

    public File getFile() {
        return file;
    }

    @Override
    public FileReader getReader() throws IOException {
        return new FileReader(this.file);
    }

    @Override
    public FileWriter getWriter() throws IOException {
        return new FileWriter(this.file);
    }

}
