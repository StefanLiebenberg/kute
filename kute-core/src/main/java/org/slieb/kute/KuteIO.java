package org.slieb.kute;

import org.apache.commons.io.IOUtils;
import org.slieb.throwables.ConsumerWithThrowable;
import org.slieb.throwables.SupplierWithThrowable;
import org.slieb.kute.api.Resource;

import java.io.*;

public class KuteIO {

    public static void serializeToStream(Serializable serializable,
                                         OutputStream outputStream) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(serializable);
        }
    }
    //
    //    public static Serializable deserializeFromStream(InputStream inputStream) throws IOException, ClassNotFoundException {
    //        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
    //            return (Serializable) objectInputStream.readObject();
    //        }
    //    }
    //
    //    public static <T> T deserialize(byte[] bytes, Class<T> classT) throws IOException, ClassNotFoundException {
    //        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    //        Serializable serializable = deserializeFromStream(bais);
    //        Preconditions.checkState(classT.isInstance(serializable));
    //        //noinspection unchecked
    //        return (T) serializable;
    //    }
    //
    //    public static byte[] serialize(Serializable object) throws IOException {
    //        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //        serializeToStream(object, baos);
    //        return baos.toByteArray();
    //    }

    /**
     * Copy all resource in provider over to some creator.
     *
     * @param provider The resource provider, or source location.
     * @param creator  The resource creator, or destination location.
     */
    public void copyProviderToCreator(Resource.Provider provider,
                                      Resource.Creator creator) {
        provider.stream().forEach((ConsumerWithThrowable<Resource.Readable, IOException>) resource ->
                copyResourceWithStreams(resource, creator.create(resource.getPath())));
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

    public static String readResource(Resource.Readable resource) throws IOException {
        try (final Reader reader = resource.getReader()) {
            return IOUtils.toString(reader);
        }
    }

    public static String readResourceUnsafe(Resource.Readable readable) {
        try {
            return KuteIO.readResource(readable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static void writeStreamToResource(InputStream inputStream,
                                             Resource.Writable writable) throws IOException {
        writable.useOutputStream(outputStream -> IOUtils.copy(inputStream, outputStream));
    }
}
