package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLResource extends AbstractResource implements Resource.InputStreaming {

    private final URL url;

    private final String path;

    public URLResource(URL url, String path) {
        this.url = url;
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    @Override
    public String getPath() {
        return path;
    }
}
