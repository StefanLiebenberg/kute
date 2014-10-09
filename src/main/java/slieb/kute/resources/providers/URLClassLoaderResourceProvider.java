package slieb.kute.resources.providers;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.ResourceProviderFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;

public class URLClassLoaderResourceProvider implements ResourceProvider<Resource.Readable> {


    private final URLClassLoader urlClassLoader;

    public URLClassLoaderResourceProvider(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    public Resource.Readable getResourceByName(String path) {
        for (URL url : urlClassLoader.getURLs()) {
            try {
                ResourceProvider<? extends Resource.Readable> resourceProvider = ResourceProviderFactory.create(url);
                Resource.Readable readable = resourceProvider.getResourceByName(path);
                if (readable != null) {
                    return readable;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public URLResourceIterable getResources() {
        return new URLResourceIterable(urlClassLoader.getURLs());
    }


    private static class URLResourceIterable implements Iterable<Resource.Readable> {

        private final URL[] urls;

        private URLResourceIterable(URL[] urls) {
            this.urls = urls;
        }

        @Override
        public Iterator<Resource.Readable> iterator() {
            return new URLResourceIterator(Arrays.asList(urls).iterator());
        }
    }

    private static class URLResourceIterator implements Iterator<Resource.Readable> {

        private final Iterator<URL> urlIterator;

        private Iterator<? extends Resource.Readable> currentResourceIterator;

        private URLResourceIterator(Iterator<URL> urlIterator) {
            this.urlIterator = urlIterator;
        }

        @Override
        public boolean hasNext() {
            cycleToNext();
            return currentResourceIterator != null && currentResourceIterator.hasNext();
        }

        @Override
        public Resource.Readable next() {
            cycleToNext();
            if (currentResourceIterator != null) {
                return currentResourceIterator.next();
            } else {
                throw new IllegalStateException("Cannot return nothing.");
            }
        }

        @Override
        public void remove() {
            urlIterator.remove();
        }

        private void cycleToNext() {
            while ((currentResourceIterator == null || !currentResourceIterator.hasNext()) && urlIterator.hasNext()) {
                URL nextURL = urlIterator.next();
                try {
                    currentResourceIterator = ResourceProviderFactory.create(nextURL).getResources().iterator();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
