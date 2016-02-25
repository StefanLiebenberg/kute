package org.slieb.kute.api;

import org.slieb.throwables.ConsumerWithThrowable;
import org.slieb.throwables.PredicateWithThrowable;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * The Resource class represents a entry on the class path.
 * <p>
 * Resources can also implement {@link Readable} or {@link Writable}
 * </p>
 * <p>Calling {@code resource.getPath()} will work on all resources.</p>
 */
public interface Resource extends Comparable<Resource> {

    Comparator<Resource> RESOURCE_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(Resource::getPath));

    /**
     * The path the the given resource. This path is not to be confused with system path of the filesystem, even some
     * overlap might exist. A resource exists within a {@link Resource.Provider} at its given path.
     *
     * @return The resource path.
     */
    String getPath();

    @Override
    default int compareTo(Resource o) {
        return Objects.compare(this, o, RESOURCE_COMPARATOR);
    }

    /**
     * Represents a readable resource. Provides getReader() and getInputStream() methods to read the resource with.
     */
    interface Readable extends Resource, Checksumable {

        /**
         * @return The charset used for the reader of this resource.
         */
        default Charset getCharset() {
            return Charset.defaultCharset();
        }

        /**
         * @return A reader that will give you the contents of resource.
         * @throws IOException an IO exception.
         */
        default Reader getReader() throws IOException {
            return new InputStreamReader(getInputStream(), getCharset());
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
        InputStream getInputStream() throws IOException;

        /**
         * <pre>{@code
         *   Resource.Readable readable =  ...;
         *   readable.useReader(reader -> {
         *      // do reader stuff.
         *   })
         * }</pre>
         *
         * @param consumerWithIO A input stream consumer that can throw IOExceptions.
         * @throws IOException throws a io exception
         */
        default void useReader(final ConsumerWithThrowable<Reader, IOException> consumerWithIO) throws IOException {
            try (Reader reader = getReader()) {
                consumerWithIO.acceptWithThrowable(reader);
            }
        }

        /**
         * <pre>{@code
         *   Resource.Readable readable =  ...;
         *   readable.useInputStream(inputStream -> {
         *      // do inputStream stuff.
         *   })
         * }</pre>
         *
         * @param consumerWithIO Some consumer for writer
         * @throws IOException throws a io exception
         */
        default void useInputStream(final ConsumerWithThrowable<InputStream, IOException> consumerWithIO) throws IOException {
            try (InputStream inputStream = getInputStream()) {
                consumerWithIO.acceptWithThrowable(inputStream);
            }
        }

        @Override
        default void updateDigest(final MessageDigest digest) throws IOException {
            useInputStream(inputStream -> digest.update(toByteArray(inputStream)));
        }
    }

    /**
     *
     */
    interface Checksumable {

        void updateDigest(MessageDigest digest) throws IOException;

        default byte[] checksum(String algorithm) throws NoSuchAlgorithmException, IOException {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            this.updateDigest(digest);
            return digest.digest();
        }
    }

    /**
     * This represents a Writable resource. You can call {@link Writable#getWriter} on it to get a writer that will
     * write to the resource.
     */
    interface Writable extends Resource {

        OutputStream getOutputStream() throws IOException;

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
         */
        default Writer getWriter() throws IOException {
            return new OutputStreamWriter(getOutputStream());
        }

        /**
         * <pre>{@code
         *   Resource.Writable writable =  ...;
         *   writable.useWriter(writer -> {
         *      // do writer stuff.
         *   })
         * }</pre>
         *
         * @param consumerWithIO Some consumer for writer
         * @throws IOException throws a io exception
         */
        default void useWriter(ConsumerWithThrowable<Writer, IOException> consumerWithIO) throws IOException {
            try (Writer writer = getWriter()) {
                consumerWithIO.acceptWithThrowable(writer);
            }
        }

        /**
         * <pre>{@code
         *   Resource.Writable writable =  ...;
         *   writable.useOutputStream(outputStream -> {
         *      // do outputStream stuff.
         *   })
         * }</pre>
         *
         * @param consumerWithIO Some consumer for writer
         * @throws IOException throws a io exception
         */
        default void useOutputStream(ConsumerWithThrowable<OutputStream, IOException> consumerWithIO) throws IOException {
            try (OutputStream writer = getOutputStream()) {
                consumerWithIO.acceptWithThrowable(writer);
            }
        }
    }

    /**
     * <p>The resource provider supplies a {@link java.lang.Iterable} of {@link Resource}.</p>
     * <p>An example would be a directory resource that supplies its contents as resource objects.</p>
     * <p>Its possible to compose resource providers in each other to get complex functionality, for example:</p>
     * <pre><code>
     *     ResourceProvider&lt;Resource.Readable&gt; resourceProvider = new FileResourceProvider(sourceDirectory);
     *     ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
     *     ResourceProvider&lt;Resource.Readable&gt; filtered = new FilteredResourceProvider(resourceProvider, filter);
     *     for(Resource.Readable resource : filtered) {
     *        // iterate through all .txt files in directory
     *         String path = resource.getPath();
     *         try(Reader reader = resource.getReader()) {
     *             ... // do stuff with reader.
     *         }
     *     }
     * </code></pre>
     * <p>Or using java 8 streams:</p>
     * <pre><code>
     *     ResourceProvider&lt;Resource.Readable&gt; resourceProvider = new FileResourceProvider(sourceDirectory);
     *     ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
     *     ResourceProvider&lt;Resource.Readable&gt; filtered = new FilteredResourceProvider(resourceProvider, filter);
     *     filtered.stream().forEach( resource -&lt; {
     *        // iterate through all .txt files in directory
     *         String path = resource.getPath();
     *         try(Reader reader = resource.getReader()) {
     *             ... // do stuff with reader.
     *         }
     *     });
     * </code></pre>
     */
    @FunctionalInterface
    interface Provider extends Iterable<Resource.Readable>, Resource.Checksumable {

        Stream<Resource.Readable> stream();

        /**
         * @return an iterator of readable resources.
         */
        @Override
        default Iterator<Resource.Readable> iterator() {
            return stream().iterator();
        }

        /**
         * @param path The path on which to find resource.
         * @return An optional resource, if it exists on the path.
         */
        default Optional<Resource.Readable> getResourceByName(String path) {
            return stream().filter(resource -> path.equals(resource.getPath())).findFirst();
        }

        /**
         * @param functionWithThrowable The function to use for each.
         * @param <E>                   An exception that might be thrown
         * @throws E A thrown exception of generic type.
         */
        default <E extends Throwable> void forEach(ConsumerWithThrowable<Resource.Readable, E> functionWithThrowable) throws E {
            for (Resource.Readable readable : this) {
                functionWithThrowable.acceptWithThrowable(readable);
            }
        }

        /**
         * @param digest The message digest to update
         * @throws IOException Throws an ioException from reading the readables.
         */
        @Override
        default void updateDigest(final MessageDigest digest) throws IOException {
            this.forEach(readable -> readable.updateDigest(digest));
        }
    }

    /**
     * The opposite of {@link Provider}
     */
    @FunctionalInterface
    interface Creator {

        Resource.Writable create(String path);
    }

    @FunctionalInterface
    interface Predicate extends PredicateWithThrowable<Resource, Throwable> {
    }
}
