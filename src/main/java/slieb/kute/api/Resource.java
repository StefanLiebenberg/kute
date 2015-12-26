package slieb.kute.api;

import slieb.kute.Kute;
import slieb.kute.utils.KuteIO;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;


/**
 * The Resource class represents a entry on the class path.
 * <p>
 * Resources can also implement {@link Readable} or {@link Writable}
 * </p>
 * <p>Calling {@code resource.getPath()} will work on all resources.</p>
 *
 * @see slieb.kute.Kute
 */
public interface Resource extends Serializable {

    /**
     * The path the the given resource. This path is not to be confused with system path of the filesystem, even some
     * overlap might exist. A resource exists within a {@link Resource.Provider} at its given path.
     *
     * @return The resource path.
     */
    String getPath();


    /**
     * Represents a readable resource. Provides getReader() and getInputStream() methods to read the resource with.
     */
    interface Readable extends Resource, Checksumable {

        /**
         * @return A reader that will give you the contents of resource.
         * @throws IOException an IO exception.
         */
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
         * @see KuteIO#readResourceWithInputStream
         */
        InputStream getInputStream() throws IOException;


        @Override
        default void updateDigest(final MessageDigest digest) throws IOException {
            digest.update(KuteIO.readResource(this).getBytes());
        }

    }

    /**
     *
     */
    interface Checksumable {

        void updateDigest(MessageDigest digest) throws IOException;

        default byte[] checksum(String algorithm) throws IOException, NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            updateDigest(digest);
            return digest.digest();
        }
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
         * @see KuteIO#writeResource
         */
        default Writer getWriter() throws IOException {
            return new OutputStreamWriter(getOutputStream());
        }


        OutputStream getOutputStream() throws IOException;
    }


    /**
     * <p>The resource provider supplies a {@link java.lang.Iterable} of {@link slieb.kute.api.Resource}.</p>
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
    interface Provider extends Iterable<Resource.Readable>, Resource.Checksumable {

        @Override
        default Iterator<Resource.Readable> iterator() {
            return stream().iterator();
        }

        Stream<Resource.Readable> stream();

        default Optional<Resource.Readable> getResourceByName(String path) {
            return Kute.findResource(stream(), path);
        }

        default <B> B collect(Collector<Resource.Readable, ?, B> collector) {
            return stream().collect(collector);
        }

        @Override
        default void updateDigest(final MessageDigest digest) throws IOException {
            for (Resource.Readable readable : this) {
                readable.updateDigest(digest);
            }
        }
    }

    /**
     * The opposite of {@link Provider}
     */
    interface Creator {
        Resource.Writable create(String path);
    }

}
