package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class URLResource
        extends AbstractResource
        implements Resource.Readable {

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
}
