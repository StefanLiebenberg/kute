package org.slieb.kute.resources;

import org.apache.commons.io.IOUtils;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.nio.charset.Charset;

public class CharsetOutputResource implements ContentResource {

    private final Resource.Readable readable;

    private final Charset outputCharset;

    public CharsetOutputResource(final Readable readable,
                                 final Charset outputCharset) {
        this.readable = readable;
        this.outputCharset = outputCharset;
    }

    @Override
    public String getContent() throws IOException {
        return IOUtils.toString(getInputStream(), readable.getCharset());
    }

    @Override
    public Charset getCharset() {
        return outputCharset;
    }

    /**
     * The path the the given resource. This path is not to be confused with system path of the filesystem, even some
     * overlap might exist. A resource exists within a {@link Provider} at its given path.
     *
     * @return The resource path.
     */
    @Override
    public String getPath() {
        return readable.getPath();
    }
}
