package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class URLResource implements Resource.Readable {

    private final URL url;

    private final String path;

    public URLResource(URL url, String path) {
        this.url = url;
        this.path = path;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(url.openStream());
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof URLResource)) return false;

        URLResource that = (URLResource) o;

        if (!path.equals(that.path)) return false;
        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
