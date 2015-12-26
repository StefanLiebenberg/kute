package slieb.kute.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.Kute;
import slieb.kute.utils.KuteDigest;
import slieb.kute.api.Resource;
import slieb.kute.resources.MutableResource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


@RunWith(MockitoJUnitRunner.class)
public class ChecksumCachedMapProviderTest {


    private MutableResource resourceA, resourceB;

    @Mock
    private Resource.Provider mockProvider;

    private ChecksumCachedMapProvider checksumProvider;

    @Before
    public void setUp() throws Exception {
        resourceA = Kute.mutableResource("/resources/a", "content A");
        resourceB = Kute.mutableResource("/resources/b", "content B");
        Resource.Checksumable checksumable = KuteDigest.join(resourceA, resourceB);
        Mockito.when(mockProvider.stream()).thenAnswer(invocation -> Stream.of(resourceA, resourceB));
        checksumProvider = new ChecksumCachedMapProvider(mockProvider, checksumable);
    }

    @Test
    public void shouldProvideSameResources() throws Exception {
        List<Resource.Readable> firstResult = checksumProvider.stream().collect(toList());
        List<Resource.Readable> secondResult = checksumProvider.stream().collect(toList());
        Assert.assertEquals(firstResult, secondResult);
        Mockito.verify(mockProvider, Mockito.times(1)).stream();
    }

    @Test
    public void shouldResourcesIfUnchanged() throws Exception {
        List<Resource.Readable> firstResult = checksumProvider.stream().collect(toList());
        resourceA.setBytes("new content a".getBytes());
        List<Resource.Readable> secondResult = checksumProvider.stream().collect(toList());
        Assert.assertNotEquals(firstResult, secondResult);
        Mockito.verify(mockProvider, Mockito.times(2)).stream();
    }
}