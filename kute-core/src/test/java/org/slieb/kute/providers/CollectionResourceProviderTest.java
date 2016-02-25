package org.slieb.kute.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.slieb.kute.Kute;
import org.slieb.kute.api.Resource;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.slieb.kute.Kute.stringResource;


@Deprecated
public class CollectionResourceProviderTest {

    Resource.Readable resourceA, resourceB, resourceC;

    Resource.Provider collectionResourceProvider;

    @Before
    public void setup() {
        resourceA = stringResource("/path/a", "contentA");
        resourceB = stringResource("/path/b", "contentB");
        resourceC = stringResource("/path/c", "contentC");
        collectionResourceProvider = Kute.providerOf(ImmutableList.of(resourceA, resourceB, resourceC));
    }

    @Test
    public void testGetResourceByName() throws Exception {
        assertEquals(Optional.of(resourceA), collectionResourceProvider.getResourceByName("/path/a"));
        assertEquals(Optional.of(resourceB), collectionResourceProvider.getResourceByName("/path/b"));
        assertEquals(Optional.of(resourceC), collectionResourceProvider.getResourceByName("/path/c"));
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
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC), builder.build());
    }
}