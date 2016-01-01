package slieb.kute.providers;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slieb.unnamed.api.FunctionWithException;
import slieb.kute.KuteIO;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;

public class ConcurrentMapResourceProviderTest implements ProviderTestInterface {

    private ConcurrentMapResourceProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new ConcurrentMapResourceProvider();
        createContent("/directory/index.html", "index content");
        createContent("/directory/other.html", "other content");
    }

    @Override
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        Assert.assertFalse(provider.stream().anyMatch(resource -> resource.getPath().equals("/directory")));
        Assert.assertTrue(provider.stream().anyMatch(resource -> resource.getPath().startsWith("/directory")));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        Assert.assertFalse(provider.getResourceByName("/directory").isPresent());
    }

    private void createContent(String path, String content) throws IOException {
        KuteIO.writeResource(provider.create(path), content);
    }

    @Override
    @Test
    public void shouldNeverReturnNullOnGetByPath() throws Exception {
        Assert.assertNotNull(provider.getResourceByName(""));
        Assert.assertNotNull(provider.getResourceByName("/no exist"));
        Assert.assertNotNull(provider.getResourceByName("/no/exist"));
    }

    @Override
    @Test
    public void shouldReturnElementsInStream() throws Exception {
        Assert.assertEquals(2, provider.stream().count());
        Assert.assertEquals(
                Sets.newHashSet(provider.getResourceByName("/directory/index.html").get(),
                        provider.getResourceByName("/directory/other.html").get())
                , provider.stream().collect(toSet()));

    }

    @Override
    @Test
    public void shouldReturnPresentOptionalInGetByPath() throws Exception {
        Assert.assertTrue(provider.getResourceByName("/directory/index.html").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        Assert.assertEquals(
                Sets.newHashSet("index content", "other content"),
                provider.stream().map(FunctionWithException.castFunctionWithException(KuteIO::readResource)).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        Assert.assertEquals("index content", KuteIO.readResource(provider.getResourceByName("/directory/index.html").get()));
        Assert.assertEquals("other content", KuteIO.readResource(provider.getResourceByName("/directory/other.html").get()));
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

    @Test
    public void shouldClearStuff() throws Exception {
        provider.clear();
        Assert.assertEquals(0, provider.stream().count());
        Assert.assertFalse(provider.getResourceByName("/directory/index.html").isPresent());
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