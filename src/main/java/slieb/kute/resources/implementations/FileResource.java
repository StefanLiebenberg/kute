package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.*;

public class FileResource extends AbstractResource implements Resource.InputStreaming, Resource.OutputStreaming {

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


    @Override
    public FileOutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
}
