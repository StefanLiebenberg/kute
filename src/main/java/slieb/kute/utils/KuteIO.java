package slieb.kute.utils;


import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;

import java.io.*;

public class KuteIO {

    public void copyProviderToCreator(Resource.Provider provider,
                                      Resource.Creator creator) {
        provider.stream().forEach(KuteLambdas.safelyConsume(
                resource -> copyResourceWithStreams(resource, creator.create(resource.getPath()))));
    }




    /**
     * Read a
     * {@link slieb.kute.api.Resource.Readable} resource from its input stream. This is almost like {@link KuteIO#readResource(Resource.Readable)},
     * except that it uses the {@link slieb.kute.api.Resource.Readable#getInputStream()} method instead.
     *
     * @param resource An {@link slieb.kute.api.Resource.Readable} resource.
     * @return The string result of reading {@link slieb.kute.api.Resource.Readable#getInputStream()}
     * @throws IOException If there is an exception getting the inputStream or reading from it.
     */
    public static String readResourceWithInputStream(Resource.Readable resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return IOUtils.toString(inputStream);
        }
    }


    /**
     * Read a {@link slieb.kute.api.Resource.Readable} resource with encoding. This is almost like {@link
     * KuteIO#readResource(Resource.Readable)}
     * (Resource.Readable)},
     * except that it uses the {@link slieb.kute.api.Resource.Readable#getInputStream()} method instead.
     *
     * @param resource An {@link slieb.kute.api.Resource.Readable} resource.
     * @param encoding The encoding with which to read the resource.
     * @return The string result of reading {@link slieb.kute.api.Resource.Readable#getInputStream()}
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
        try (Writer writer = resource.getWriter()) {
            IOUtils.write(content.toString(), writer);
        }
    }


    public static void writeResourceWithOutputStream(final Resource.Writable resource,
                                                     final CharSequence content) throws IOException {
        try (OutputStream stream = resource.getOutputStream()) {
            IOUtils.write(content.toString(), stream);
        }
    }

    public static void writeResourceWithOutputStream(final Resource.Writable resource,
                                                     final CharSequence content,
                                                     final String encoding) throws IOException {
        try (OutputStream stream = resource.getOutputStream()) {
            IOUtils.write(content.toString(), stream, encoding);
        }
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
        try (Reader reader = readable.getReader(); Writer writer = writable.getWriter()) {
            IOUtils.copy(reader, writer);
        }
    }

    public static void copyResourceWithStreams(Resource.Readable inputStreaming,
                                               Resource.Writable outputStreaming) throws IOException {
        try (InputStream inputStream = inputStreaming.getInputStream();
             OutputStream outputStream = outputStreaming.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    public static byte[] readBytes(Resource.Readable resource) throws IOException {
        return readResource(resource).getBytes();
    }

    public static String readResource(Resource.Readable resource) throws IOException {
        try (final Reader reader = resource.getReader()) {
            return IOUtils.toString(reader);
        }
    }


}
