package slieb.kute.providers;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.resources.MutableBytesArrayResource;
import slieb.kute.utils.KuteDigest;
import slieb.kute.utils.KuteIO;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static slieb.kute.utils.KuteIO.readResource;
import static slieb.kute.utils.KuteLambdas.unsafeMap;
import static slieb.kute.utils.KutePredicates.resourceEquals;


public class ChecksumCachedMapProviderTest implements ProviderTestInterface {

    private MutableBytesArrayResource resourceA, resourceB;

    private Resource.Provider rawProvider;

    private ChecksumCachedMapProvider provider;

    @Before
    public void setUp() throws Exception {
        resourceA = Kute.mutableResource("/resources/a", "content A");
        resourceB = Kute.mutableResource("/resources/b", "content B");
        Resource.Checksumable checksumable = KuteDigest.join(resourceA, resourceB);
        rawProvider = Kute.providerOf(resourceA, resourceB);
        provider = new ChecksumCachedMapProvider(rawProvider, checksumable);
    }






    @Override
    @Test
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        Assert.assertFalse(provider.stream().anyMatch(resource -> resource.getPath().equals("/resources")));
        Assert.assertTrue(provider.stream().anyMatch(resource -> resource.getPath().startsWith("/resources")));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        Assert.assertFalse(provider.getResourceByName("/resources").isPresent());
        Assert.assertTrue(provider.getResourceByName("/resources/b").isPresent());
    }

    @Override
    @Test
    public void shouldNeverReturnNullOnGetByPath() throws Exception {
        Assert.assertNotNull(provider.getResourceByName("does/not/exist"));
        Assert.assertNotNull(provider.getResourceByName("att all"));
        Assert.assertNotNull(provider.getResourceByName("$%@/243/52/45"));
    }

    @Override
    @Test
    public void shouldReturnElementsInStream() throws Exception {
        Assert.assertEquals(2, provider.stream().count());
    }

    @Override
    @Test
    public void shouldReturnPresentOptionalInGetByPath() throws Exception {
        Assert.assertTrue(provider.getResourceByName("/resources/a").isPresent());
        Assert.assertTrue(provider.getResourceByName("/resources/b").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        Assert.assertEquals(
                Sets.newHashSet("content A", "content B"),
                provider.stream().map(unsafeMap(KuteIO::readResource)).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        Assert.assertEquals("content A", readResource(provider.getResourceByName("/resources/a").get()));
        Assert.assertEquals("content B", readResource(provider.getResourceByName("/resources/b").get()));
    }

    @Override
    @Test
    public void shouldReturnAllResourcesInStreamInGetByPath() throws Exception {
        for (Resource.Readable readable : provider) {
            Optional<Resource.Readable> optional = provider.getResourceByName(readable.getPath());
            Assert.assertTrue(optional.isPresent());
            Assert.assertEquals(readable, optional.get());
            Assert.assertTrue(resourceEquals(readable, optional.get()));
        }
    }

    @Override
    @Test
    public void shouldBeSerializable() throws Exception {
        Resource.Provider loaded = KuteIO.deserialize(KuteIO.serialize(provider), Resource.Provider.class);
        Assert.assertEquals(provider.toString(), loaded.toString());
        Assert.assertEquals(provider.hashCode(), loaded.hashCode());
        Assert.assertEquals(provider, loaded);
    }
}