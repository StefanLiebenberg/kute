package slieb.kute.resources.providers;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static slieb.kute.Kute.group;
import static slieb.kute.Kute.resourceProviderToSet;

@RunWith(MockitoJUnitRunner.class)
public class GroupResourceProviderTest {
//
//    public GroupResourceProvider<? extends Resource.Readable> createReadableGroup() {
//        List<ResourceProvider<? extends Resource.Readable>> list = new ArrayList<>();
//        list.add(new FileResourceProvider(null));
//        return new GroupResourceProvider<>(list);
//    }

    @Mock
    Resource resourceA, resourceB, resourceC, resourceD;

    @Before
    public void setup() {
        Mockito.when(resourceA.getPath()).thenReturn("/resourceA");
        Mockito.when(resourceB.getPath()).thenReturn("/resourceB");
        Mockito.when(resourceC.getPath()).thenReturn("/resourceC");
        Mockito.when(resourceD.getPath()).thenReturn("/resourceD");

    }


//    @Test
//    public void createGroupedFileResources() throws Exception {
//        ResourceProvider<? extends Resource.Readable> readable = createReadableGroup();
//    }

    @Test
    public void testGetDistinctResources() throws Exception {

        ResourceProvider<Resource> group1 = Kute.providerOf(resourceA, resourceB);
        ResourceProvider<Resource> group2 = Kute.providerOf(resourceB, resourceC);
        ResourceProvider<Resource> group3 = Kute.providerOf(resourceC, resourceD);
        ResourceProvider<Resource> all = group(group1, group2, group3);

        assertEquals(Optional.of(resourceA), all.getResourceByName("/resourceA"));
        assertEquals(Optional.of(resourceB), all.getResourceByName("/resourceB"));
        assertEquals(Optional.of(resourceC), all.getResourceByName("/resourceC"));
        assertEquals(Optional.of(resourceD), all.getResourceByName("/resourceD"));

        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC, resourceD), resourceProviderToSet(all));

    }

    @Test
    public void testGetDistinctByPathResources() throws Exception {
        Mockito.when(resourceC.getPath()).thenReturn("/resourceB");
        ResourceProvider<Resource> group1 = Kute.providerOf(resourceA, resourceB);
        ResourceProvider<Resource> group2 = Kute.providerOf(resourceC, resourceD);
        ResourceProvider<Resource> all = group(group1, group2);
        assertEquals(Optional.of(resourceA), all.getResourceByName("/resourceA"));
        assertEquals(Optional.of(resourceB), all.getResourceByName("/resourceB"));
        assertEquals(Optional.empty(), all.getResourceByName("/resourceC"));
        assertEquals(Optional.of(resourceD), all.getResourceByName("/resourceD"));
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceD), resourceProviderToSet(all));
    }


}