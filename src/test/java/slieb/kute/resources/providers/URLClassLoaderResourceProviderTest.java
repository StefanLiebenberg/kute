package slieb.kute.resources.providers;

import org.junit.Before;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;
import slieb.kute.resources.filters.AnyFilter;
import slieb.kute.resources.filters.PatternFilter;

import java.net.URLClassLoader;
import java.util.regex.Pattern;

public class URLClassLoaderResourceProviderTest {

    private URLClassLoaderResourceProvider urlClassLoader;

    @Before
    public void setUp() throws Exception {
        urlClassLoader = new URLClassLoaderResourceProvider((URLClassLoader) getClass().getClassLoader());
    }

    @Test
    public void testGetResourceByNamespace() throws Exception {

    }

    @Test
    public void testGetResources() throws Exception {
        ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
        ResourceFilter classFilter = new PatternFilter(Pattern.compile("org/.*\\.class"));
        ResourceFilter anyFilter = new AnyFilter(filter, classFilter);
        FilteredResourceProvider<Resource.Readable> filtered = new FilteredResourceProvider<>(urlClassLoader, anyFilter);
        for (Resource resource : urlClassLoader.getResources()) {
            System.out.println(resource.getPath());
        }
    }
}