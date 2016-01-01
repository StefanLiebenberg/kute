package slieb.kute.api;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.Kute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static slieb.kute.Kute.filterResources;
import static slieb.kute.Kute.providerOf;


public final class ResourceProviderTest {

    Resource.Readable resourceA, resourceB, resourceC;

    @Before
    public void setup() {
        resourceA = Kute.stringResource("/path/a", "A");
        resourceB = Kute.stringResource("/path/b", "B");
        resourceC = Kute.stringResource("/path/c", "C");
    }


    @Test
    public void testCollectionResources() throws Exception {
        testProvider(providerOf(resourceA, resourceB, resourceC));
    }

    @Test
    public void testFilteredResourceProvider() throws Exception {
        filterResources(providerOf(resourceA, resourceB, resourceC), resource -> true);
    }


    protected void testGetResourceByName(Resource.Provider resourceProvider) throws Exception {
        assertEquals(Optional.of(resourceA), resourceProvider.getResourceByName("/path/a"));
        assertEquals(Optional.of(resourceB), resourceProvider.getResourceByName("/path/b"));
        assertEquals(Optional.of(resourceC), resourceProvider.getResourceByName("/path/c"));
        assertFalse(resourceProvider.getResourceByName("/path/d").isPresent());
    }


    protected void testIterator(Resource.Provider provider) throws Exception {
        Iterator<Resource.Readable> iterator = provider.iterator();
        Set<Resource> resources = new HashSet<>();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            assertFalse(resources.contains(resource));
            resources.add(resource);
        }
        assertEquals(3, resources.size());
    }

    protected void testStream(Resource.Provider provider) throws Exception {
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC), provider.stream().collect(toSet()));
    }

    protected void testProvider(Resource.Provider provider) throws Exception {
        testGetResourceByName(provider);
        testIterator(provider);
        testStream(provider);
    }
}

