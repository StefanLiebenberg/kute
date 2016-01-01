package slieb.kute.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.resources.MutableBytesArrayResource;
import slieb.kute.KuteDigest;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RunWith(MockitoJUnitRunner.class)
public class ChecksumCachedMapProviderMockitoTest {

    private MutableBytesArrayResource resourceA, resourceB;

    @Mock
    private Resource.Provider mockProvider;

    private ChecksumCachedMapProvider provider;

    @Before
    public void setUp() throws Exception {
        resourceA = Kute.mutableResource("/resources/a", "content A");
        resourceB = Kute.mutableResource("/resources/b", "content B");
        Resource.Checksumable checksumable = KuteDigest.join(resourceA, resourceB);
        Mockito.when(mockProvider.stream()).thenAnswer(invocation -> Stream.of(resourceA, resourceB));
        Mockito.when(mockProvider.getResourceByName(Mockito.anyString())).thenCallRealMethod();
        Mockito.when(mockProvider.iterator()).thenCallRealMethod();
        provider = new ChecksumCachedMapProvider(mockProvider, checksumable);
    }

    @Test
    public void shouldProvideSameResources() throws Exception {
        List<Resource.Readable> firstResult = provider.stream().collect(toList());
        List<Resource.Readable> secondResult = provider.stream().collect(toList());
        Assert.assertEquals(firstResult, secondResult);
        Mockito.verify(mockProvider, Mockito.times(1)).stream();
    }


    @Test
    public void shouldResourcesIfUnchanged() throws Exception {
        List<Resource.Readable> firstResult = provider.stream().collect(toList());
        resourceA.setBytes("new content a".getBytes());
        List<Resource.Readable> secondResult = provider.stream().collect(toList());
        Assert.assertNotEquals(firstResult, secondResult);
        Mockito.verify(mockProvider, Mockito.times(2)).stream();
    }


}