package slieb.kute.resources;

import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.implementations.*;
import slieb.kute.resources.providers.MappedResourceProvider;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Resources {
    private Resources() {
    }

    public static String readResource(Resource.Readable resource) throws IOException {
        try (Reader reader = resource.getReader()) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            return writer.toString();
        }
    }

    public static void writeResource(Resource.Writeable resource, String content) throws IOException {
        try (Writer writer = resource.getWriter()) {
            IOUtils.write(content, writer);
        }
    }

    public static void copyResource(Resource.Readable readable, Resource.Writeable writeable) throws IOException {
        try (Reader reader = readable.getReader(); Writer writer = writeable.getWriter()) {
            IOUtils.copy(reader, writer);
        }
    }

    public static FileResource fileResource(File file) {
        return new FileResource(file);
    }

    public static FileResource fileResource(File file, String path) {
        return new FileResource(file, path);
    }

    public static <A extends Resource> RenamedPathResource<A> rename(A resource, String path) {
        return new RenamedPathResource<>(resource, path);
    }

    public static InputStreamResource inputStreamResource(InputStream inputStream, String path) {
        return new InputStreamResource(inputStream, path);
    }

    public static MemoryCacheResource cacheResource(Resource.Readable resource) {
        return new MemoryCacheResource(resource);
    }

    public static StringResource stringResource(String content, String path) {
        return new StringResource(content, path);
    }

    public static URLResource urlResource(URL url, String path) {
        return new URLResource(url, path);
    }

    public static ZipEntryResource zipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        return new ZipEntryResource(zipFile, zipEntry);
    }

    public static RenamedPathResource<ZipEntryResource> zipEntryResource(ZipFile zipFile, ZipEntry zipEntry, String path) {
        return rename(zipEntryResource(zipFile, zipEntry), path);
    }


    /**
     * Builds a stream
     * <p>
     * Note: This copies the entries into a stream via Stream.builder(), so its lazy.
     *
     * @param resourceProvider the resource provider.
     * @param <R>              the resource interface
     * @return a Stream interface build from the resource provider
     */
    public static <R extends Resource> Stream<R> resourceProviderToStream(ResourceProvider<R> resourceProvider) {
        Stream.Builder<R> builder = Stream.builder();
        for (R resource : resourceProvider.getResources()) {
            builder.add(resource);
        }
        return builder.build();
    }

    public static <R extends Resource> List<R> resourceProviderToList(ResourceProvider<R> resourceProvider) {
        return resourceProviderToStream(resourceProvider).collect(toList());
    }

    public static <R extends Resource> Set<R> resourceProviderToSet(ResourceProvider<R> resourceProvider) {
        return resourceProviderToStream(resourceProvider).collect(toSet());
    }

    public static <A extends Resource, B extends Resource> ResourceProvider<B> mapResources(
            ResourceProvider<A> provider, Function<A, B> function) {
        return new MappedResourceProvider<>(provider, function);
    }
}
