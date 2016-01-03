package slieb.kute.providers;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.Kute;
import slieb.kute.api.Resource;

import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static slieb.kute.Kute.group;

@RunWith(MockitoJUnitRunner.class)
public class GroupResourceProviderTest {
//
//    public GroupResourceProvider<? extends Resource.Readable> createReadableGroup() {
//        List<ResourceProvider<? extends Resource.Readable>> list = new ArrayList<>();
//        list.add(new FileResourceProvider(null));
//        return new GroupResourceProvider<>(list);
//    }

    @Mock
    Resource.Readable resourceA, resourceB, resourceC, resourceD;

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

        Resource.Provider group1 = Kute.providerOf(resourceA, resourceB);
        Resource.Provider group2 = Kute.providerOf(resourceB, resourceC);
        Resource.Provider group3 = Kute.providerOf(resourceC, resourceD);
        Resource.Provider all = group(group1, group2, group3);

        assertEquals(Optional.of(resourceA), all.getResourceByName("/resourceA"));
        assertEquals(Optional.of(resourceB), all.getResourceByName("/resourceB"));
        assertEquals(Optional.of(resourceC), all.getResourceByName("/resourceC"));
        assertEquals(Optional.of(resourceD), all.getResourceByName("/resourceD"));

        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceC, resourceD), all.collect(toSet()));

    }

    @Test
    public void testGetDistinctByPathResources() throws Exception {
        Mockito.when(resourceC.getPath()).thenReturn("/resourceB");
        Resource.Provider group1 = Kute.providerOf(resourceA, resourceB);
        Resource.Provider group2 = Kute.providerOf(resourceC, resourceD);
        Resource.Provider all = group(group1, group2);
        assertEquals(Optional.of(resourceA), all.getResourceByName("/resourceA"));
        assertEquals(Optional.of(resourceB), all.getResourceByName("/resourceB"));
        assertEquals(Optional.empty(), all.getResourceByName("/resourceC"));
        assertEquals(Optional.of(resourceD), all.getResourceByName("/resourceD"));
        assertEquals(ImmutableSet.of(resourceA, resourceB, resourceD), all.collect(toSet()));
    }


}