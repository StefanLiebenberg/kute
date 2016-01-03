package slieb.kute.providers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URLClassLoader;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class URLClassLoaderResourceProviderTest {

    @Mock
    private URLClassLoader mockUrlClassloader;

    private URLClassLoaderResourceProvider classLoaderResource;

    @Before
    public void setUp() throws Exception {
        classLoaderResource = new URLClassLoaderResourceProvider(mockUrlClassloader);
    }

    @Test
    public void testGetResourceWithUnknownPathWillReturnEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), classLoaderResource.getResourceByName("/some-path"));
    }


}