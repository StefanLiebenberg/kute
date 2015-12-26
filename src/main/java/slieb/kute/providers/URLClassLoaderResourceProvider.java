package slieb.kute.providers;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;


public class URLClassLoaderResourceProvider extends AbstractURLResourceProvider {

    private final URLClassLoader urlClassLoader;

    public URLClassLoaderResourceProvider(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    @Override
    protected Stream<URL> urlStream() {
        return Optional.ofNullable(urlClassLoader.getURLs()).map(Arrays::stream).orElseGet(Stream::empty);
    }
}

