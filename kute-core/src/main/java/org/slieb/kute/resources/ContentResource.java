package org.slieb.kute.resources;

import org.apache.commons.io.IOUtils;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * Represents a String based Resource.
 */
public interface ContentResource extends Resource.Readable {

    String getContent() throws IOException;

    @Override
    default Reader getReader() throws IOException {
        return new StringReader(getContent());
    }

    @Override
    default InputStream getInputStream() throws IOException {
        return IOUtils.toInputStream(getContent(), getCharset());
    }
}
