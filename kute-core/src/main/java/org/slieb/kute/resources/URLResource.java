package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLResource implements Resource.Readable {

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
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof URLResource)) { return false; }

        final URLResource that = (URLResource) o;

        if (path != null ? !path.equals(that.path) : that.path != null) { return false; }
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
