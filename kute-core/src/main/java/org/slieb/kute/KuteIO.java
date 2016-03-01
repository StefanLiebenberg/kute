package org.slieb.kute;

import org.apache.commons.io.IOUtils;
import org.slieb.kute.api.Resource;
import org.slieb.throwables.SupplierWithThrowable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import static org.slieb.throwables.ConsumerWithThrowable.castConsumerWithThrowable;
import static org.slieb.throwables.FunctionWithThrowable.castFunctionWithThrowable;

public class KuteIO {

    private KuteIO() {}

    /**
     * Copy all resource in provider over to some creator.
     *
     * @param provider The resource provider, or source location.
     * @param creator  The resource creator, or destination location.
     */
    public static void copyProviderToCreator(final Resource.Provider provider,
                                             final Resource.Creator creator) {
        provider.stream().forEach(castConsumerWithThrowable((resource) -> copyResourceWithStreams(resource, creator.create(resource.getPath()))));
    }

    /**
     * Read a
     * {@link Resource.Readable} resource from its input stream. This is almost like {@link KuteIO#readResource(Resource.Readable)},
     * except that it uses the {@link Resource.Readable#getInputStream()} method instead.
     *
     * @param resource An {@link Resource.Readable} resource.
     * @return The string result of reading {@link Resource.Readable#getInputStream()}
     * @throws IOException If there is an exception getting the inputStream or reading from it.
     */
    public static String readResourceWithInputStream(Resource.Readable resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return IOUtils.toString(inputStream);
        }
    }

    /**
     * Read a {@link Resource.Readable} resource with encoding. This is almost like {@link
     * KuteIO#readResource(Resource.Readable)}
     * (Resource.Readable)},
     * except that it uses the {@link Resource.Readable#getInputStream()} method instead.
     *
     * @param resource An {@link Resource.Readable} resource.
     * @param encoding The encoding with which to read the resource.
     * @return The string result of reading {@link Resource.Readable#getInputStream()}
     * @throws IOException If there is an exception getting the inputStream or reading from it.
     */
    public static String readResourceWithInputStream(final Resource.Readable resource,
                                                     final String encoding) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return IOUtils.toString(inputStream, encoding);
        }
    }

    /**
     * Write content to a Writable Resource.
     *
     * @param resource a writable resource instance.
     * @param content  The content that will be writen to the resource.
     * @throws IOException a IOException can occur during the write process.
     */
    public static void writeResource(final Resource.Writable resource,
                                     final CharSequence content) throws IOException {
        resource.useWriter(writer -> IOUtils.write(content.toString(), writer));
    }

    public static void writeResourceWithOutputStream(final Resource.Writable resource,
                                                     final CharSequence content) throws IOException {
        resource.useOutputStream(stream -> IOUtils.write(content.toString(), stream));
    }

    public static void writeResourceWithOutputStream(final Resource.Writable resource,
                                                     final CharSequence content,
                                                     final String encoding) throws IOException {
        resource.useOutputStream(stream -> IOUtils.write(content.toString(), stream, encoding));
    }

    /**
     * Copy the content of one resources to another.
     *
     * @param readable A readable resource instance.
     * @param writable A writable resource instance.
     * @throws IOException A io exception can occur during the copy process.
     */
    public static void copyResource(Resource.Readable readable,
                                    Resource.Writable writable) throws IOException {
        writeResourceToStream(readable, writable::getOutputStream);
    }

    public static void copyResourceWithStreams(Resource.Readable readable,
                                               Resource.Writable writable) throws IOException {
        readable.useInputStream(istream -> writable.useOutputStream(ostream -> IOUtils.copy(istream, ostream)));
    }

    public static byte[] toByteArray(Resource.Readable resource) throws IOException {
        return readResource(resource).getBytes();
    }

    /**
     * @param resource A readable resource.
     * @return The string content of resource.
     * @throws IOException
     */
    public static String readResource(Resource.Readable resource) throws IOException {
        try (final Reader reader = resource.getReader()) {
            return IOUtils.toString(reader);
        }
    }

    /**
     * @param readable The readable resource.
     * @return The resource content.
     * @deprecated The method will be removed.
     */
    @Deprecated
    public static String readResourceUnsafe(Resource.Readable readable) {
        return castFunctionWithThrowable(KuteIO::readResource).apply(readable);
    }

    public static void writeResourceToStream(Resource.Readable readable,
                                             OutputStream out) throws IOException {
        readable.useInputStream(inputStream -> IOUtils.copy(inputStream, out));
    }

    public static void writeResourceToStream(Resource.Readable readable,
                                             SupplierWithThrowable<OutputStream, IOException> out) throws IOException {
        try (OutputStream withThrowable = out.getWithThrowable()) {
            writeResourceToStream(readable, withThrowable);
        }
    }

    public static void writeStreamToResource(SupplierWithThrowable<InputStream, IOException> inputSupplier,
                                             Resource.Writable writable) throws IOException {
        writeStreamToResource(inputSupplier.getWithThrowable(), writable);
    }

    /**
     * @param inputStream The input stream.
     * @param writable    The writable resource.
     * @throws IOException
     */
    public static void writeStreamToResource(final InputStream inputStream,
                                             final Resource.Writable writable) throws IOException {
        writable.useOutputStream(outputStream -> IOUtils.copy(inputStream, outputStream));
    }
}
