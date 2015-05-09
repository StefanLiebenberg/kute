package slieb.kute.resources;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.implementations.*;
import slieb.kute.resources.providers.CollectionResourceProvider;
import slieb.kute.resources.providers.FilteredResourceProvider;
import slieb.kute.resources.providers.GroupResourceProvider;
import slieb.kute.resources.providers.MappedResourceProvider;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Resources {

    /**
     * Read content from a Readable resource.
     *
     * @param resource A readable resource instance.
     * @return The resource as provided by the resource's reader.
     * @throws IOException an IOException may occur.
     */
    public static String readResource(Resource.Readable resource) throws IOException {
        try (Reader reader = resource.getReader()) {
            return IOUtils.toString(reader);
        }
    }

    public static String readStreamResource(Resource.InputStreaming resource) throws IOException {
        try (InputStream istream = resource.getInputStream()) {
            return IOUtils.toString(istream);
        }
    }


    public static String readStreamResource(Resource.InputStreaming resource, String encoding) throws IOException {
        try (InputStream istream = resource.getInputStream()) {
            return IOUtils.toString(istream, encoding);
        }
    }

    /**
     * Write content to a Writeable Resource.
     *
     * @param resource a writeable resource instance.
     * @param content  The content that will be writen to the resource.
     * @throws IOException a IOException can occur during the write process.
     */
    public static void writeResource(Resource.Writeable resource, CharSequence content) throws IOException {
        try (Writer writer = resource.getWriter()) {
            IOUtils.write(content.toString(), writer);
        }
    }

    public static void writeStreamResource(Resource.OutputStreaming resource, CharSequence content) throws IOException {
        try (OutputStream stream = resource.getOutputStream()) {
            IOUtils.write(content.toString(), stream);
        }
    }

    public static void writeStreamResource(Resource.OutputStreaming resource, CharSequence content, String encoding) throws IOException {
        try (OutputStream stream = resource.getOutputStream()) {
            IOUtils.write(content.toString(), stream, encoding);
        }
    }

    /**
     * Copy the content of one resources to another.
     *
     * @param readable  A readable resource instance.
     * @param writeable A writeable resource instance.
     * @throws IOException A io exception can occur during the copy process.
     */
    public static void copyResource(Resource.Readable readable, Resource.Writeable writeable) throws IOException {
        try (Reader reader = readable.getReader(); Writer writer = writeable.getWriter()) {
            IOUtils.copy(reader, writer);
        }
    }

    public static void copyResourceAsStreams(Resource.InputStreaming inputStreaming, Resource.OutputStreaming outputStreaming) throws IOException {
        try (InputStream inputStream = inputStreaming.getInputStream();
             OutputStream outputStream = outputStreaming.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    /**
     * Create a file Resources.
     *
     * @param file A file that will provide the contents and path of this resource.
     * @return A file resource that will provide the file contents when needed.
     */
    public static FileResource fileResource(File file) {
        return new FileResource(file);
    }

    /**
     * Create a file resource.
     *
     * @param file A file that will provide the contents of this resource.
     * @param path The path this resource should be available at.
     * @return A file resource that will provide the file contents when needed.
     */
    public static FileResource fileResource(String path, File file) {
        return new FileResource(path, file);
    }

    /**
     * Create a resource with alternate name
     *
     * @param resource Any resource.
     * @param path     The new path location for this resource.
     * @param <A>      extends Resource.
     * @return A resource of type A extends Resource that is a copy of the old resource, but with a new name.
     */
    public static <A extends Resource> RenamedPathResource<A> rename(A resource, String path) {
        return new RenamedPathResource<>(path, resource);
    }

    /**
     * Create resources from input stream.
     *
     * @param supplier The input stream to supply content.
     * @param path     The path name for this resource.
     * @return a readable resource that reads from provided input stream.
     */
    public static InputStreamResource inputStreamResource(Supplier<InputStream> supplier, String path) {
        return new InputStreamResource(supplier, path);
    }

    /**
     * Create a resource that cache's its response.
     *
     * @param resource A readable resource instance.
     * @return A readable resource that will cache the contents of another readable resource, to speed things up. Use with caution.
     */
    public static CachedResource cacheResource(Resource.Readable resource) {
        return new CachedResource(resource);
    }

    public static StringSupplierResource stringResource(String path, CharSequence content) {
        return new StringSupplierResource(path, content.toString());
    }


    public static URLResource urlResource(String path, URL url) {
        return new URLResource(path, url);
    }

    public static ZipEntryResource zipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        return new ZipEntryResource(zipFile, zipEntry);
    }

    public static RenamedPathResource<ZipEntryResource> zipEntryResource(ZipFile zipFile, ZipEntry zipEntry, String path) {
        return rename(zipEntryResource(zipFile, zipEntry), path);
    }


    public static <R extends Resource> List<R> resourceProviderToList(ResourceProvider<? extends R> resourceProvider) {
        return resourceProvider.stream().collect(toList());
    }

    public static <R extends Resource> Set<R> resourceProviderToSet(ResourceProvider<? extends R> resourceProvider) {
        return resourceProvider.stream().collect(toSet());
    }

    public static <A extends Resource, B extends Resource> ResourceProvider<B> mapResources(
            ResourceProvider<A> provider, Function<A, B> function) {
        return new MappedResourceProvider<>(provider, function);
    }


    public static <A extends Resource> ResourceProvider<A> filterResources(ResourceProvider<A> provider, ResourceFilter filter) {
        return new FilteredResourceProvider<>(provider, filter);
    }

    public static <A extends Resource> ResourceProvider<A> filterResources(ResourceProvider<A> provider, Predicate<Resource> predicate) {
        return filterResources(provider, (ResourceFilter) predicate::test);

    }

    public static <A extends Resource> ResourceProvider<A> providerOf(Collection<A> resources) {
        return new CollectionResourceProvider<>(resources);
    }

    @SafeVarargs
    public static <A extends Resource> ResourceProvider<A> providerOf(A... resources) {
        return providerOf(ImmutableList.copyOf(resources));
    }

    @SafeVarargs
    public static <A extends Resource> ResourceProvider<A> group(ResourceProvider<A>... providers) {
        return new GroupResourceProvider<>(ImmutableList.copyOf(providers));
    }


    @SuppressWarnings("unchecked")
    public static <A extends Resource, B> B getResourceAs(A resource, Class<B> classObject) {
        Preconditions.checkState(resource.getClass().isAssignableFrom(classObject));
        return (B) resource;
    }
}
