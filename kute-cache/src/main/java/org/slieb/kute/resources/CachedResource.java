package org.slieb.kute.resources;

import org.slieb.kute.KuteIO;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * A very basic lazy cache
 */
public class CachedResource implements Resource.Readable {

    private final Resource.Readable cachedResource;

    /**
     * @param readable A readable resource to cache
     */
    public CachedResource(final Readable readable) throws IOException {
        this.cachedResource = new BytesArrayResource(readable.getPath(), KuteIO.toByteArray(readable));
    }

    /**
     * <p>Returns a inputStream for the resource.</p>
     * <pre>{@code
     *      Resource.InputStreaming resource = ...;
     *      try(InputStream intputStream = resource.getInputStream()) {
     *          // .. do stuff with intputStream
     *      }
     * }</pre>
     *
     * @return A inputStream that will read from resource.
     * @throws IOException when there is an exception creating the InputStream.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return cachedResource.getInputStream();
    }

    /**
     * The path the the given resource. This path is not to be confused with system path of the filesystem, even some
     * overlap might exist. A resource exists within a {@link Provider} at its given path.
     *
     * @return The resource path.
     */
    @Override
    public String getPath() {
        return cachedResource.getPath();
    }
}
