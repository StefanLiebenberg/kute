package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;

import java.nio.charset.Charset;

/**
 * An abstract implementation of Resource.Readable, taking care of path and charset properties.
 */
public abstract class AbstractResource implements Resource.Readable {

    protected final String path;

    protected final Charset charset;

    /**
     * @param path    The resource path.
     * @param charset The resource charset.
     */
    protected AbstractResource(final String path,
                               final Charset charset) {
        this.path = path;
        this.charset = charset;
    }

    /**
     * @param path The resource path.
     */
    public AbstractResource(final String path) {
        this(path, Charset.defaultCharset());
    }

    /**
     * @return The resource path.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * @return The resource charset.
     */
    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof AbstractResource)) { return false; }

        final AbstractResource that = (AbstractResource) o;

        if (path != null ? !path.equals(that.path) : that.path != null) { return false; }
        return charset != null ? charset.equals(that.charset) : that.charset == null;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AbstractResource{" +
                "charset=" + charset +
                ", path='" + path + '\'' +
                '}';
    }
}
