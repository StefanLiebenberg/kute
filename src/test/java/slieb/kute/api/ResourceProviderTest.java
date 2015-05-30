package slieb.kute.api;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.Kute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static slieb.kute.Kute.filterResources;
import static slieb.kute.Kute.providerOf;


public class ResourceProviderTest {

    Resource resourceA, resourceB, resourceC;

    @Before
    public void setup() {
        resourceA = Kute.stringResource("/path/a", "A");
        resourceB = Kute.stringResource("/path/b", "B");
        resourceC = Kute.stringResource("/path/c", "C");
    }


    public void testGetResourceByName(ResourceProvider resourceProvider) throws Exception {
        assertEquals(resourceA, resourceProvider.getResourceByName("/path/a"));
        assertEquals(resourceB, resourceProvider.getResourceByName("/path/b"));
        assertEquals(resourceC, resourceProvider.getResourceByName("/path/c"));
        assertNull(resourceProvider.getResourceByName("/path/d"));
    }

    public void testIterator(ResourceProvider<Resource> provider) throws Exception {
        Iterator<Resource> iterator = provider.iterator();
        Set<Resource> resources = new HashSet<>();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            assertFalse(resources.contains(resource));
            resources.add(resource);
        }
        assertEquals(3, resources.size());
    }

    public void testStream(ResourceProvider<Resource> provider) throws Exception {
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC), provider.stream().collect(toSet()));
    }

    public void testProvider(ResourceProvider<Resource> provider) throws Exception {
        testGetResourceByName(provider);
        testIterator(provider);
        testStream(provider);
    }

    @Test
    public void testCollectionResources() throws Exception {
        testProvider(providerOf(resourceA, resourceB, resourceC));
    }

    @Test
    public void testFilteredResourceProvider() throws Exception {
        filterResources(providerOf(resourceA, resourceB, resourceC), resource -> true);
    }
}

