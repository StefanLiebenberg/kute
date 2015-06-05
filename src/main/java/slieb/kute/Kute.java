package slieb.kute;


import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.ResourceException;
import slieb.kute.resources.implementations.*;
import slieb.kute.resources.providers.*;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static slieb.kute.resources.ResourcePredicates.NON_NULL;
import static slieb.kute.resources.ResourcePredicates.distinctFilter;

public class Kute {

    /**
     * Provides the {@link ClassLoader} instance. Found from {@code Thread.currentThread().getContextClassLoader()} or {@code Kute.class.getClassLoader()}
     *
     * @return The default classloader.
     */
    public static ClassLoader getDefaultClassLoader() {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (Exception ignored) {
            return Kute.class.getClassLoader();
        }
    }

    /**
     * Provides a {@link ResourceProvider} that wraps around the default classLoader.
     *
     * @return A default provider that wraps a resource provider around a classLoader.
     * @see Kute#getDefaultClassLoader()
     * @see Kute#getProvider(ClassLoader)
     */
    public static ResourceProvider<Resource.InputStreaming> getDefaultProvider() {
        return getProvider(getDefaultClassLoader());
    }

    /**
     * @param urls An array of urls where the provider can search for resources. Only jar and directory urls are supported.
     * @return An {@link URLArrayResourceProvider} class that will provide all the resources in the array of urls.
     */
    public static URLArrayResourceProvider getProvider(List<URL> urls) {
        return new URLArrayResourceProvider(urls);
    }


    /**
     * @param classLoader The classloader from which to search for resources. Currently only implementations of {@link URLClassLoader} are scanned.
     * @return A Resource provider that will scan the classloader.
     */
    public static ResourceProvider<Resource.InputStreaming> getProvider(ClassLoader classLoader) {
        List<URL> urls = new ArrayList<>();
        while (classLoader != null) {
            if (classLoader instanceof URLClassLoader) {
                Collections.addAll(urls, ((URLClassLoader) classLoader).getURLs());
            }
            classLoader = classLoader.getParent();
        }
        return getProvider(urls);
    }


    /**
     * @param resources A var_arg array of resources that the provider will contain.
     * @param <T>       extends any implementation of @{link Resource}
     * @return A {@link CollectionResourceProvider} that contains all of the specified resources.
     */
    @SafeVarargs
    public static <T extends Resource> CollectionResourceProvider<T> providerOf(T... resources) {
        return providerOf(Arrays.asList(resources));
    }

    /**
     * Returns a empty resource provider.
     *
     * @param <A> Types that the provider supplies.
     * @return a resource provider that returns nothing.
     */
    public <A extends Resource> ResourceProvider<A> emptyProvider() {
        return new EmptyProvider<>();
    }

    /**
     * @param resources A collection of resources that the provider will contain.
     * @param <T>       extends any implementation of @{link Resource}
     * @return A {@link CollectionResourceProvider} that contains all of the specified resources.
     */
    public static <T extends Resource> CollectionResourceProvider<T> providerOf(Collection<T> resources) {
        return new CollectionResourceProvider<>(resources);
    }

    /**
     * Group ResourceProviders together into one resources Resource Provider.
     *
     * {@code
     * ResourceProvider<? extends Resource.Readable> providerA = Kute.directoryProvider(new File("images/"));
     * ResourceProvider<? extends Resource.Readable> classpathProvider = Kute.getDefaultProvider();
     * ResourceProvider<? extends Resource.Readable> provider = Kute.group(providerA, classpathProvider);
     * }
     *
     * @param providers The resource providers to group.
     * @param <A>       The implementation of Resource
     * @param <B>       The implementation of ResourceProvider
     * @return A resource provider that providers resources for all the given resource providers.
     */
    @SafeVarargs
    public static <A extends Resource, B extends ResourceProvider<A>> ResourceProvider<A> group(B... providers) {
        return new GroupResourceProvider<>(ImmutableList.copyOf(providers));
    }


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

    /**
     * Read the resource, but throws a RuntimeException instead of a IOException.
     *
     * @param resource a Readable resource.
     * @return The content of resource.
     */
    public static String readResourceUnsafe(Resource.Readable resource) {
        try {
            return readResource(resource);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    /**
     * Read a {@link Resource.InputStreaming} resource. This is almost like {@link Kute#readResource(Resource.Readable)},
     * except that it uses the {@link Resource.InputStreaming#getInputStream()} method instead.
     *
     * @param resource An {@link Resource.InputStreaming} resource.
     * @return The string result of reading {@link Resource.InputStreaming#getInputStream()}
     * @throws IOException If there is an exception getting the inputStream or reading from it.
     */
    public static String readStreamResource(Resource.InputStreaming resource) throws IOException {
        try (InputStream istream = resource.getInputStream()) {
            return IOUtils.toString(istream);
        }
    }


    /**
     * Read a {@link Resource.InputStreaming} resource with encoding. This is almost like {@link Kute#readResource(Resource.Readable)},
     * except that it uses the {@link Resource.InputStreaming#getInputStream()} method instead.
     *
     * @param resource An {@link Resource.InputStreaming} resource.
     * @param encoding The encoding with which to read the resource.
     * @return The string result of reading {@link Resource.InputStreaming#getInputStream()}
     * @throws IOException If there is an exception getting the inputStream or reading from it.
     */
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
    public static void copyReadableResource(Resource.Readable readable, Resource.Writeable writeable) throws IOException {
        try (Reader reader = readable.getReader(); Writer writer = writeable.getWriter()) {
            IOUtils.copy(reader, writer);
        }
    }

    public static void copyStreamingResource(Resource.InputStreaming inputStreaming, Resource.OutputStreaming outputStreaming) throws IOException {
        try (InputStream inputStream = inputStreaming.getInputStream();
             OutputStream outputStream = outputStreaming.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    public static void copyResource(Resource.Readable input, Resource.Writeable output) throws IOException {
        if (input instanceof Resource.InputStreaming && output instanceof Resource.OutputStreaming) {
            copyStreamingResource((Resource.InputStreaming) input, (Resource.OutputStreaming) output);
        } else {
            copyReadableResource(input, output);
        }
    }

    public static void copyResourceUnsafe(Resource.Readable input, Resource.Writeable output) {
        try {
            copyResource(input, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Create a file Resource
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
    public static <A extends Resource> RenamedPathResource<A> renameResource(String path, A resource) {
        return new RenamedPathResource<>(path, resource);
    }

    /**
     * Create resources from input stream.
     *
     * @param supplier The input stream to supply content.
     * @param path     The path name for this resource.
     * @return a readable resource that reads from provided input stream.
     */
    public static InputStreamResource inputStreamResource(String path, Supplier<InputStream> supplier) {
        return new InputStreamResource(path, supplier);
    }

    /**
     * Create resources from input stream.
     *
     * @param supplier The input stream to supply content.
     * @param path     The path name for this resource.
     * @return a readable resource that reads from provided input stream.
     */
    public static InputStreamResource inputStreamResourceWithIO(String path, InputStreamResource.SupplierWithIO<InputStream> supplier) {
        return new InputStreamResource(path, supplier);
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


    public static RenamedPathResource<Resource.InputStreaming> zipEntryResource(String path, ZipFile zipFile, ZipEntry zipEntry) {
        return renameResource(path, zipEntryResource(zipFile, zipEntry));
    }


    public static <R extends Resource> List<R> resourceProviderToList(ResourceProvider<? extends R> resourceProvider) {
        return resourceProvider.stream().collect(toList());
    }

    public static <R extends Resource> Set<R> resourceProviderToSet(ResourceProvider<? extends R> resourceProvider) {
        return resourceProvider.stream().collect(toSet());
    }


    public static <A extends Resource, B extends Resource> ResourceProvider<B> mapResources(ResourceProvider<A> provider, Function<A, B> function) {
        return new MappedResourceProvider<>(provider, function);
    }

    public static <A extends Resource> ResourceProvider<A> filterResources(ResourceProvider<A> provider, Predicate<? super Resource> predicate) {
        return new FilteredResourceProvider<>(provider, predicate);
    }


    @SuppressWarnings("unchecked")
    public static <A extends Resource, B> B getResourceAs(A resource, Class<B> classObject) {
        Preconditions.checkState(classObject.isAssignableFrom(resource.getClass()), "Resource is not a " + classObject);
        return (B) resource;
    }


    public static Resource.InputStreaming zipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        return inputStreamResourceWithIO(zipEntry.getName(), () -> zipFile.getInputStream(zipEntry));
    }


    public static <R extends Resource> R findFirstResource(Stream<R> stream) {
        return stream.filter(NON_NULL).findFirst().orElse(null);
    }


    /**
     * Finds the first resource in stream that matches given path.
     *
     * @param stream A stream of resources.
     * @param path   The path to search for.
     * @param <R>    The resource implementation.
     * @return A matching resource, if found.
     */
    public static <R extends Resource> R findResource(Stream<R> stream, String path) {
        return findFirstResource(stream.filter(r -> r.getPath().equals(path)));
    }

    /**
     * @param stream   The unfiltered resource stream.
     * @param function A function to map resources into values that we will check for duplicates on.
     * @param <R>      The generic resource implementation.
     * @param <X>      The generic value type.
     * @return A stream without resource duplicates as determined by the passed function.
     */
    public static <R extends Resource, X> Stream<R> distinct(Stream<R> stream, Function<R, X> function) {
        return stream.filter(distinctFilter(function));
    }

    /**
     * @param stream The resource stream.
     * @param <R>    The resource implementation.
     * @return A stream that has been filtered from resources with duplicate paths.
     */
    public static <R extends Resource> Stream<R> distinctPath(Stream<R> stream) {
        return distinct(stream, Resource::getPath);
    }


    public void copyProviderToCreator(ResourceProvider<? extends Resource.Readable> provider,
                                      ResourceProvider.ResourceCreator<?> creator) {
        provider.stream().forEach(resource -> copyResourceUnsafe(resource, creator.create(resource.getPath())));
    }


}
