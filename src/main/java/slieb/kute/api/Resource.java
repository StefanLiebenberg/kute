package slieb.kute.api;

import java.io.*;

import static slieb.kute.Kute.getResourceAs;


/**
 * The Resource class represents a entry on the class path.
 * <p>
 * Resources can also implement one of the interfaces {@link slieb.kute.api.Resource.Readable},
 * {@link Writable}, {@link slieb.kute.api.Resource.InputStreaming},
 * {@link slieb.kute.api.Resource.OutputStreaming} or {@link slieb.kute.api.Resource.Proxy}
 * </p>
 * <p>Calling {@code resource.getPath()} will work on all resources.</p>
 *
 * @see slieb.kute.Kute
 */
public interface Resource extends Serializable {

    /**
     * The path the the given resource. This path is not to be confused with system path of the filesystem, even some
     * overlap might exist. A resource exists within a {@link ResourceProvider} at its given path.
     *
     * @return The resource path.
     */
    String getPath();


    /**
     * The readable version of Resource. Includes a getReader method.
     */
    interface Readable extends Resource {
        /**
         * @return A reader that will give you the contents of resource.
         * @throws IOException an IO exception.
         */
        Reader getReader() throws IOException;

    }


    /**
     * This represents a Writable resource. You can call {@link Writable#getWriter} on it to get a writer that will
     * write to the resource.
     */
    interface Writable extends Resource {

        /**
         * <b>Using the Writer</b>
         * <pre>{@code
         *      Resource.Writable resource = ...;
         *      try(Writer writer  = resource.getWriter()) {
         *          // .. do stuff with writer
         *      }
         * }</pre>
         *
         * @return A writer that will write to resource.
         * @throws IOException when the writer is created.
         * @see slieb.kute.Kute#writeResource
         */
        Writer getWriter() throws IOException;

    }

    /**
     *
     */
    interface InputStreaming extends Resource.Readable {

        @Override
        default Reader getReader() throws IOException {
            return new InputStreamReader(getInputStream());
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
         * @see slieb.kute.Kute#readStreamResource
         */
        InputStream getInputStream() throws IOException;
    }

    /**
     *
     */
    interface OutputStreaming extends Writable {

        @Override
        default Writer getWriter() throws IOException {
            return new OutputStreamWriter(getOutputStream());
        }

        OutputStream getOutputStream() throws IOException;
    }

    /**
     * Proxy resources point to other resources that may or may not implement any of Readable, Writable,
     * OutputStreaming,
     * InputStreaming. The proxy class does implement all of those interfaces, but using the methods should throw an
     * error if the proxied resource does not implement the right interface.
     *
     * @param <A> Any interface that extends Resource.
     * @see slieb.kute.resources.implementations.AbstractProxy
     */
    interface Proxy<A extends Resource> extends Resource, Readable, Writable, OutputStreaming, InputStreaming {

        A getResource();

        /**
         * This method proxies {@code getReader()} to it's composed resource's {@code getReader()} method, if and
         * only if that resource
         * implements {@link slieb.kute.api.Resource.Readable}, otherwise it throws an IllegalStateException.
         *
         * @return The proxied {@link Reader} object.
         * @throws IOException           when there was an exception creating the {@link Reader}.
         * @throws IllegalStateException when the composed resource does not implement
         *                               {@link slieb.kute.api.Resource.Readable}
         */
        @Override
        default Reader getReader() throws IOException {
            return getResourceAs(getResource(), Readable.class).getReader();
        }

        /**
         * This method proxies getOutputStream() to it's composed resource, if and only if that resource
         * implements {@link slieb.kute.api.Resource.OutputStreaming}, otherwise it throws an IllegalStateException.
         *
         * @return The proxied outputStream.
         * @throws IOException           when there was an exception creating the outputStream.
         * @throws IllegalStateException when the composed resource does not implement
         *                               {@link slieb.kute.api.Resource.OutputStreaming}
         */
        @Override
        default Writer getWriter() throws IOException {
            return getResourceAs(getResource(), Writable.class).getWriter();
        }

        /**
         * This method proxies getOutputStream() to it's composed resource, if and only if that resource
         * implements {@link slieb.kute.api.Resource.OutputStreaming}, otherwise it throws an IllegalStateException.
         *
         * @return The proxied outputStream.
         * @throws IOException           when there was an exception creating the outputStream.
         * @throws IllegalStateException when the composed resource does not implement
         *                               {@link slieb.kute.api.Resource.OutputStreaming}
         */
        @Override
        default InputStream getInputStream() throws IOException {
            return getResourceAs(getResource(), InputStreaming.class).getInputStream();
        }

        /**
         * This method proxies getOutputStream() to it's composed resource, if and only if that resource
         * implements {@link slieb.kute.api.Resource.OutputStreaming}, otherwise it throws an IllegalStateException.
         *
         * @return The proxied outputStream.
         * @throws IOException           when there was an exception creating the outputStream.
         * @throws IllegalStateException when the composed resource does not implement
         *                               {@link slieb.kute.api.Resource.OutputStreaming}
         */
        @Override
        default OutputStream getOutputStream() throws IOException {
            return getResourceAs(getResource(), OutputStreaming.class).getOutputStream();
        }
    }
}
