package slieb.kute;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.URLArrayResourceProvider;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Kute {

    private Kute() {
    }


    public static ResourceProvider<Resource.Readable> getDefaultProvider() {
        try {
            return getProvider(Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
        }

        return getProvider(Kute.class.getClassLoader());
    }

    public static ResourceProvider<Resource.Readable> getProvider(ClassLoader classLoader) {
        List<URL> urls = new ArrayList<>();
        while (classLoader != null) {
            if (classLoader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                    urls.add(url);
                }
            }
            classLoader = classLoader.getParent();
        }
        return new URLArrayResourceProvider(urls);
    }
}
