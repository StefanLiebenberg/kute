package slieb.kute.resources.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.resources.Resources;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


public class CollectionResourceProviderTest {

    Resource resourceA, resourceB, resourceC;

    CollectionResourceProvider<Resource> collectionResourceProvider;

    @Before
    public void setup() {
        resourceA = Resources.stringResource("contentA", "/path/a");
        resourceB = Resources.stringResource("contentA", "/path/a");
        resourceC = Resources.stringResource("contentA", "/path/a");
        collectionResourceProvider =
                new CollectionResourceProvider<>(ImmutableList.of(resourceA, resourceB, resourceC));
    }

    @Test
    public void testGetResourceByName() throws Exception {
        assertEquals(resourceA, collectionResourceProvider.getResourceByName("/path/a"));
        assertEquals(resourceB, collectionResourceProvider.getResourceByName("/path/a"));
        assertEquals(resourceC, collectionResourceProvider.getResourceByName("/path/a"));
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