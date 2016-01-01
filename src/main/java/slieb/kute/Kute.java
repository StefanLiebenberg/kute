package slieb.kute;


import org.slieb.unnamed.api.FunctionWithException;
import org.slieb.unnamed.api.SupplierWithException;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourcePredicate;
import slieb.kute.providers.*;
import slieb.kute.resources.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Kute {

    /**
     * Provides the {@link ClassLoader} instance. Found from {@code Thread.currentThread().getContextClassLoader()}
     * or {@code Kute.class.getClassLoader()}
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
     * Provides a {@link slieb.kute.api.Resource.Provider} that wraps around the default classLoader.
     *
     * @return A default provider that wraps a resource provider around a classLoader.
     * @see Kute#getDefaultClassLoader()
     * @see Kute#getProvider(ClassLoader)
     */
    public static Resource.Provider getDefaultProvider() {
        return getProvider(getDefaultClassLoader());
    }

    /**
     * @param urls An array of urls where the provider can search for resources. Only jar and directory urls are
     *             supported.
     * @return An {@link URLArrayResourceProvider} class that will provide all the resources in the array of urls.
     */
    public static URLArrayResourceProvider getProvider(List<URL> urls) {
        return new URLArrayResourceProvider(urls);
    }


    /**
     * @param classLoader The classloader from which to search for resources. Currently only implementations of
     *                    {@link URLClassLoader} are scanned.
     * @return A Resource provider that will scan the classloader.
     */
    public static Resource.Provider getProvider(ClassLoader classLoader) {
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
     * Returns a empty resource provider.
     *
     * @return a resource provider that returns nothing.
     */
    public static Resource.Provider emptyProvider() {
        return Stream::empty;
    }


    /**
     * @param resources A var_arg array of resources that the provider will contain.
     * @return A {@link slieb.kute.api.Resource.Provider} that contains all of the specified resources.
     */
    public static Resource.Provider providerOf(Resource.Readable... resources) {
        return providerOf(Arrays.asList(resources));
    }

    /**
     * @param resources A collection of resources that the provider will contain.
     * @return A {@link slieb.kute.api.Resource.Provider} that contains all of the specified resources.
     */
    public static Resource.Provider providerOf(Collection<Resource.Readable> resources) {
        return new CollectionProvider(resources);
    }

    /**
     * Group ResourceProviders together into one resources Resource Provider.
     * <p>
     * {@code
     * Resource.Provider providerA = Kute.directoryProvider(new File("images/"));
     * Resource.Provider classpathProvider = Kute.getDefaultProvider();
     * Resource.Provider provider = Kute.group(providerA, classpathProvider);
     * }
     *
     * @param providers The resource providers to group.
     * @return A resource provider that providers resources for all the given resource providers.
     */
    public static Resource.Provider group(Resource.Provider... providers) {
        return group(() -> Arrays.stream(providers));
    }

    public static Resource.Provider group(Collection<Resource.Provider> providers) {
        return group(providers::stream);
    }


    public static Resource.Provider group(Supplier<Stream<Resource.Provider>> providers) {
        return () -> distinctPath(providers.get().flatMap(Resource.Provider::stream));
    }


    public static Resource.Readable immutableMemoryResource(Resource.Readable readable) throws IOException {
        return Kute.resourceWithBytes(readable.getPath(), KuteIO.readBytes(readable));
    }


    /**
     * Creates a file resource provider.
     *
     * @param directory A directory
     * @return A Resource Provider that provides files from directory with paths relative to the directory.
     */
    public static FileResourceProvider provideFrom(File directory) {
        return new FileResourceProvider(directory);
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
    public static FileResource fileResource(String path,
                                            File file) {
        return new FileResource(path, file);
    }

    /**
     * Create a resource with alternate name
     *
     * @param resource Any resource.
     * @param path     The new path location for this resource.
     * @return A resource of type A extends Resource that is a copy of the old resource, but with a new name.
     */
    public static RenamedPathResource renameResource(String path,
                                                     Resource.Readable resource) {
        return new RenamedPathResource(path, resource);
    }

    /**
     * Create resources from input stream.
     *
     * @param path     The path name for this resource.
     * @param supplier The input stream to supply content.
     * @return a readable resource that reads from provided input stream.
     */
    public static InputStreamResource inputStreamResource(final String path,
                                                          final SupplierWithException<InputStream, IOException> supplier) {
        return new InputStreamResource(path, supplier);
    }

    /**
     * Create resources from input stream.
     *
     * @param supplier The input stream to supply content.
     * @param path     The path name for this resource.
     * @return a readable resource that reads from provided input stream.
     */
    public static OutputStreamResource outputStreamResource(final String path,
                                                            final SupplierWithException<OutputStream, IOException> supplier) {
        return new OutputStreamResource(path, supplier);
    }

    public static Resource.Readable resourceWithBytes(final String path, final byte[] bytes) {
        return new BytesArrayResource(path, bytes);
    }


    /**
     * Create a resource that cache's its response.
     *
     * @param resource A readable resource instance.
     * @return A readable resource that will cache the contents of another readable resource, to speed things up. Use
     * with caution.
     */
    public static CachedResource cacheResource(Resource.Readable resource) {
        return new CachedResource(resource);
    }


    public static Resource.Readable stringResource(String path, String content) {
        return resourceWithBytes(path, content.getBytes());
    }

    public static Resource.Readable stringResource(String path, Supplier<String> supplier) {
        return new StringSupplierResource(path, supplier);
    }

    public static URLResource urlResource(final String path,
                                          final URL url) {
        return new URLResource(path, url);
    }


    public static RenamedPathResource zipEntryResource(String path,
                                                       ZipFile zipFile,
                                                       ZipEntry zipEntry) {
        return renameResource(path, zipEntryResource(zipFile, zipEntry));
    }


    public static Resource.Provider mapResources(final Resource.Provider provider, final FunctionWithException<Resource.Readable, Resource.Readable, IOException> function) {
        return new MappedResourceProvider(provider, function);
    }

    public static Resource.Provider filterResources(Resource.Provider provider, ResourcePredicate<Resource> predicate) {
        return new FilteredResourceProvider(provider, predicate);
    }

    public static Resource.Readable zipEntryResource(final ZipFile zipFile,
                                                     ZipEntry zipEntry) {
        return inputStreamResource(zipEntry.getName(), () -> zipFile.getInputStream(zipEntry));
    }


    public static <R extends Resource> Optional<R> findFirstResource(Stream<R> stream) {
        return stream.filter(KuteLambdas.nonNull()).findFirst();
    }

    public static <R extends Resource> Optional<R> findFirstOptionalResource(Stream<Optional<R>> optionalStream) {
        return optionalStream.filter(Optional::isPresent).map(Optional::get).findFirst();
    }


    /**
     * Finds the first resource in stream that matches given path.
     *
     * @param stream A stream of resources.
     * @param path   The path to search for.
     * @param <R>    The resource implementation.
     * @return A matching resource, if found.
     */
    public static <R extends Resource> Optional<R> findResource(Stream<R> stream,
                                                                String path) {
        return findFirstResource(stream.filter(r -> r.getPath().equals(path)));
    }

    /**
     * @param stream   The unfiltered resource stream.
     * @param function A function to map resources into values that we will check for duplicates on.
     * @param <R>      The generic resource implementation.
     * @param <X>      The generic value type.
     * @return A stream without resource duplicates as determined by the passed function.
     */
    public static <R extends Resource, X> Stream<R> distinct(final Stream<R> stream,
                                                             final FunctionWithException<R, X, IOException> function) {
        return stream.filter(KuteLambdas.distinctFilter(function));
    }

    /**
     * @param stream The resource stream.
     * @param <R>    The resource implementation.
     * @return A stream that has been filtered from resources with duplicate paths.
     */
    public static <R extends Resource> Stream<R> distinctPath(Stream<R> stream) {
        return distinct(stream, Resource::getPath);
    }

    public static MutableBytesArrayResource mutableResource(String s, String s1) {
        return new MutableBytesArrayResource(s, s1.getBytes());
    }

    public static Resource resource(String path) {
        return new NamedResource(path);
    }
}
