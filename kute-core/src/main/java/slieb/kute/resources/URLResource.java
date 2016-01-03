package slieb.kute.resources;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

public class URLResource implements Resource.Readable, Serializable {
    
    private final String path;
    private final URL url;

    public URLResource(final String path,
                       final URL url) {
        this.path = path;
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
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
        return Objects.equals(path, that.path) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, url);
    }

    @Override
    public String toString() {
        return "URLResource{" +
                "path='" + path + '\'' +
                ", url=" + url +
                '}';
    }
}
