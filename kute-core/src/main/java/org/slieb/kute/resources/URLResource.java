package org.slieb.kute.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class URLResource extends AbstractResource {

    private final URL url;

    /**
     * @param path The resource path.
     * @param url  The resource url.
     */
    public URLResource(final String path,
                       final URL url) {
        super(path);
        this.url = url;
    }

    /**
     * @param path    The resource path.
     * @param url     The resource url.
     * @param charset The resource charset.
     */
    public URLResource(final String path,
                       final URL url,
                       final Charset charset) {
        super(path, charset);
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof URLResource)) { return false; }
        if (!super.equals(o)) { return false; }

        final URLResource that = (URLResource) o;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "URLResource{" +
                "url=" + url +
                "} " + super.toString();
    }
}
