package slieb.kute.resources.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.api.Resource;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static slieb.kute.Kute.stringResource;


public class CollectionResourceProviderTest {

    Resource resourceA, resourceB, resourceC;

    CollectionResourceProvider<Resource> collectionResourceProvider;

    @Before
    public void setup() {
        resourceA = stringResource("/path/a", "contentA");
        resourceB = stringResource("/path/b", "contentB");
        resourceC = stringResource("/path/c", "contentC");
        collectionResourceProvider =
                new CollectionResourceProvider<>(ImmutableList.of(resourceA, resourceB, resourceC));
    }

    @Test
    public void testGetResourceByName() throws Exception {
        assertEquals(resourceA, collectionResourceProvider.getResourceByName("/path/a"));
        assertEquals(resourceB, collectionResourceProvider.getResourceByName("/path/b"));
        assertEquals(resourceC, collectionResourceProvider.getResourceByName("/path/c"));
    }

    @Test
    public void testStream() throws Exception {
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC),
                collectionResourceProvider.stream().collect(Collectors.toSet()));
    }

    @Test
    public void testIterator() throws Exception {
        ImmutableSet.Builder<Resource> builder = new ImmutableSet.Builder<>();
        for (Resource resource : collectionResourceProvider) {
            builder.add(resource);
        }
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC),
                builder.build());
    }
}