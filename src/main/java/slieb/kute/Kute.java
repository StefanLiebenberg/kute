package slieb.kute;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.URLArrayResourceProvider;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kute {

    public static ResourceProvider<Resource.InputStreaming> getProvider(List<URL> urls) {
        return new URLArrayResourceProvider(urls);
    }

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

    public static ResourceProvider<Resource.InputStreaming> getDefaultProvider() {
        try {
            return getProvider(Thread.currentThread().getContextClassLoader());
        } catch (Exception ignored) {
        }
        return getProvider(Kute.class.getClassLoader());
    }
}
