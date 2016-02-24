package slieb.kute.resources;

import slieb.kute.api.Resource;

import java.io.*;

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
        return new ByteArrayInputStream(getContent().getBytes());
    }
}
