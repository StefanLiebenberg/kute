package slieb.kute.providers;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.utils.KuteIO;

import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static slieb.kute.utils.KuteIO.readResource;
import static slieb.kute.utils.KuteLambdas.unsafeMap;


public class RenamedNamespaceProviderTest implements ProviderTestInterface {


    private RenamedNamespaceProvider provider;
    private ConcurrentMapResourceProvider rawProvider;

    @Before
    public void setUp() throws Exception {
        rawProvider = new ConcurrentMapResourceProvider();
        provider = new RenamedNamespaceProvider(rawProvider, "/directory", "/folder");
        createContent("/directory/index.html", "index content");
        createContent("/directory/other.html", "other content");
    }

    private void createContent(String path, String content) throws Exception {
        KuteIO.writeResource(rawProvider.create(path), content);
    }


    @Override
    @Test
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        Assert.assertFalse(provider.stream().anyMatch(resource -> resource.getPath().equals("/folder")));
        Assert.assertTrue(provider.stream().anyMatch(resource -> resource.getPath().startsWith("/folder")));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        Assert.assertFalse(provider.getResourceByName("/folder").isPresent());
        Assert.assertTrue(provider.getResourceByName("/folder/index.html").isPresent());
    }

    @Override
    @Test
    public void shouldNeverReturnNullOnGetByPath() throws Exception {
        Assert.assertNotNull(provider.getResourceByName(null));
        Assert.assertNotNull(provider.getResourceByName(""));
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
        Assert.assertTrue(provider.getResourceByName("/folder/other.html").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        Assert.assertEquals(
                Sets.newHashSet("index content", "other content"),
                provider.stream().map(unsafeMap(KuteIO::readResource)).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        Assert.assertEquals("index content", readResource(provider.getResourceByName("/folder/index.html").get()));
        Assert.assertEquals("other content", readResource(provider.getResourceByName("/folder/other.html").get()));
    }

    @Override
    @Test
    public void shouldReturnAllResourcesInStreamInGetByPath() throws Exception {
        provider.stream().forEach(readable -> {
            Optional<Resource.Readable> optional = provider.getResourceByName(readable.getPath());
            Assert.assertTrue(optional.isPresent());
            Assert.assertEquals(readable, optional.get());
        });
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