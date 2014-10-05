package slieb.kute.resources.api;

import slieb.kute.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class InputStreamResource implements Resource.Readable {

    private final InputStream inputStream;
    private final String path;

    public InputStreamResource(InputStream inputStream, String path) {
        this.inputStream = inputStream;
        this.path = path;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(this.inputStream);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputStreamResource that = (InputStreamResource) o;

        if (!inputStream.equals(that.inputStream)) return false;
        if (!path.equals(that.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inputStream.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
