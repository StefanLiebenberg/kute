package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.*;

/**
 * Warning, this is a use once resource. Avoid if possible.
 */
public class OutputStreamResource implements Resource.Writeable, Closeable {

    private final OutputStream outputStream;

    private final String path;

    public OutputStreamResource(OutputStream outputStream, String path) {
        this.outputStream = outputStream;
        this.path = path;
    }

    @Override
    public Writer getWriter() throws IOException {
        return new OutputStreamWriter(outputStream);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutputStreamResource)) return false;

        OutputStreamResource that = (OutputStreamResource) o;

        if (!outputStream.equals(that.outputStream)) return false;
        if (!path.equals(that.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = outputStream.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

}
