package slieb.kute.resources.implementations;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLResource extends AbstractResource implements Resource.InputStreaming {

    private final URL url;

    public URLResource(final String path,
                       final URL url) {
        super(path);
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

}
