package slieb.kute;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.CollectionResourceProvider;
import slieb.kute.resources.providers.URLArrayResourceProvider;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Kute {

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
     * @return A default provider that wraps a resource provider around a classLoader.
     */
    public static ResourceProvider<Resource.InputStreaming> getDefaultProvider() {
        try {
            return getProvider(Thread.currentThread().getContextClassLoader());
        } catch (Exception ignored) {
        }
        return getProvider(Kute.class.getClassLoader());
    }

    /**
     * @param resources A var_arg array of resources that the provider will contain.
     * @param <T>       extends any implementation of @{link Resource}
     * @return A {@link CollectionResourceProvider} that contains all of the specified resources.
     */
    public static <T extends Resource> CollectionResourceProvider<T> providerOf(T... resources) {
        return providerOf(Arrays.asList(resources));
    }

    /**
     * @param resources A collection of resources that the provider will contain.
     * @param <T>       extends any implementation of @{link Resource}
     * @return A {@link CollectionResourceProvider} that contains all of the specified resources.
     */
    public static <T extends Resource> CollectionResourceProvider<T> providerOf(Collection<T> resources) {
        return new CollectionResourceProvider<>(resources);
    }

}
