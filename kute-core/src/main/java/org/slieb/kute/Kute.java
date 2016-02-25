package org.slieb.kute;

import org.slieb.kute.api.Resource;
import org.slieb.kute.providers.DirectoryProvider;
import org.slieb.kute.providers.MappedResourceProvider;
import org.slieb.kute.providers.URLArrayResourceProvider;
import org.slieb.kute.resources.BytesArrayResource;
import org.slieb.kute.resources.ContentSupplierResource;
import org.slieb.kute.resources.FileResource;
import org.slieb.kute.resources.RenamedPathResource;
import org.slieb.throwables.FunctionWithThrowable;
import org.slieb.throwables.SupplierWithThrowable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static org.slieb.kute.KutePredicates.distinctFilter;

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
     * Provides a {@link Resource.Provider} that wraps around the default classLoader.
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
     * @return A {@link Resource.Provider} that contains all of the specified resources.
     */
    public static Resource.Provider providerOf(Resource.Readable... resources) {
        return providerOf(Arrays.asList(resources));
    }

    /**
     * @param resources A collection of resources that the provider will contain.
     * @return A {@link Resource.Provider} that contains all of the specified resources.
     */
    public static Resource.Provider providerOf(Collection<Resource.Readable> resources) {
        return resources::stream;
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
        return Kute.resourceWithBytes(readable.getPath(), KuteIO.toByteArray(readable));
    }

    /**
     * Creates a file resource provider.
     *
     * @param directory A directory
     * @return A Resource Provider that provides files from directory with paths relative to the directory.
     */
    public static DirectoryProvider provideFrom(File directory) {
        return new DirectoryProvider(directory);
    }

    /**
     * Create a file Resource
     *
     * @param file A file that will provide the contents and path of this resource.
     * @return A file resource that will provide the file contents when needed.
     */
    public static FileResource fileResource(File file) {
        return fileResource(file.getPath(), file);
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

    public static Resource.Readable resourceWithBytes(final String path,
                                                      final byte[] bytes) {
        return new BytesArrayResource(path, bytes);
    }

    public static Resource.Readable stringResource(final String path,
                                                   final String content) {
        return new ContentSupplierResource(path, () -> content);
    }

    public static Resource.Readable stringResource(String path,
                                                   SupplierWithThrowable<String, IOException> supplier) {
        return new ContentSupplierResource(path, supplier);
    }

    public static Resource.Provider mapResources(final Resource.Provider provider,
                                                 final FunctionWithThrowable<Resource.Readable, Resource.Readable, IOException> function) {
        return new MappedResourceProvider(provider, function);
    }

    public static Resource.Provider filterResources(Resource.Provider provider,
                                                    Resource.Predicate predicate) {
        return () -> provider.stream().filter(predicate);
    }

    public static <R extends Resource> Optional<R> findFirstResource(Stream<R> stream) {
        return stream.filter(KutePredicates.nonNull()).findFirst();
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
     * @param stream The resource stream.
     * @param <R>    The resource implementation.
     * @return A stream that has been filtered from resources with duplicate paths.
     */
    public static <R extends Resource> Stream<R> distinctPath(Stream<R> stream) {
        return stream.sorted(comparing(Resource::getPath)).filter(distinctFilter(Resource::getPath));
    }
}
