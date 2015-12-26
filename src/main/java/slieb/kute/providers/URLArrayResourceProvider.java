package slieb.kute.providers;


import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class URLArrayResourceProvider extends AbstractURLResourceProvider {

    private final List<URL> urls;

    public URLArrayResourceProvider(List<URL> urls) {
        this.urls = urls;
    }

    @Override
    protected Stream<URL> urlStream() {
        return urls.stream();
    }
}
