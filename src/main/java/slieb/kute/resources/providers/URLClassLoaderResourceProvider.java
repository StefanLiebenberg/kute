package slieb.kute.resources.providers;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class URLClassLoaderResourceProvider extends AbstractURLResourceProvider {

    private final URLClassLoader urlClassLoader;

    public URLClassLoaderResourceProvider(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    @Override
    protected Stream<URL> urlStream() {
        return asList(urlClassLoader.getURLs()).stream();
    }
}

