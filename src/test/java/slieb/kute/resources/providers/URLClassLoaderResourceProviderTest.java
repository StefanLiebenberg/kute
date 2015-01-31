package slieb.kute.resources.providers;

import org.junit.Before;
import org.junit.Test;

import java.net.URLClassLoader;

public class URLClassLoaderResourceProviderTest {

    private URLClassLoaderResourceProvider urlClassLoader;

    @Before
    public void setUp() throws Exception {
        urlClassLoader = new URLClassLoaderResourceProvider((URLClassLoader) getClass().getClassLoader());
    }

    @Test
    public void testGetResourceByNamespace() throws Exception {

    }
}