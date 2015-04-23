package slieb.kute.resources.providers;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Stream;

public class URLClassLoaderResourceProvider extends AbstractURLResourceProvider {

    private final URLClassLoader urlClassLoader;

    public URLClassLoaderResourceProvider(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    @Override
    protected Stream<URL> getURLStream() {
        return Arrays.asList(urlClassLoader.getURLs()).stream();
    }
}

