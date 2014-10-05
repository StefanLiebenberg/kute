package slieb.kute.resources.providers;


import slieb.kute.resources.Resource;
import slieb.kute.resources.ResourceProvider;
import slieb.kute.resources.special.URLToResourceProviderFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;

public class URLClassLoaderResourceProvider implements ResourceProvider<Resource.Readable> {

    private static final URLToResourceProviderFactory FACTORY = new URLToResourceProviderFactory();

    private final URLClassLoader urlClassLoader;

    public URLClassLoaderResourceProvider(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    public Resource.Readable getResourceByName(String path) {
        for (URL url : urlClassLoader.getURLs()) {
            try {
                ResourceProvider<? extends Resource.Readable> resourceProvider = FACTORY.create(url);
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
                return null;
            }
        }

        @Override
        public void remove() {
            urlIterator.remove();
        }

        private void cycleToNext() {
            while (!(currentResourceIterator != null && currentResourceIterator.hasNext()) && urlIterator.hasNext()) {
                URL nextURL = urlIterator.next();
                try {
                    currentResourceIterator = FACTORY.create(nextURL).getResources().iterator();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
