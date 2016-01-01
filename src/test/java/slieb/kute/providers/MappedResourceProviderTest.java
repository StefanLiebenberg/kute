package slieb.kute.providers;

import com.google.common.collect.Sets;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slieb.throwables.FunctionWithException;
import slieb.kute.Kute;
import slieb.kute.KuteDigest;
import slieb.kute.KuteIO;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.slieb.throwables.FunctionWithException.castFunctionWithException;
import static slieb.kute.KuteIO.readResource;


public class MappedResourceProviderTest implements ProviderTestInterface {

    private MappedResourceProvider provider;
    private ChecksumMap function;
    private ConcurrentMapResourceProvider rawProvider;

    @Before
    public void setUp() throws Exception {
        rawProvider = new ConcurrentMapResourceProvider();
        function = new ChecksumMap();
        provider = new MappedResourceProvider(rawProvider, function);
        createContent("/directory/index.html", "index content");
        createContent("/directory/other.html", "other content");
    }

    private void createContent(String path, String content) throws Exception {
        KuteIO.writeResource(rawProvider.create(path), content);
    }


    @Override
    @Test
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        Assert.assertFalse(provider.stream().anyMatch(resource -> resource.getPath().equals("/directory")));
        Assert.assertTrue(provider.stream().anyMatch(resource -> resource.getPath().startsWith("/directory")));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        Assert.assertFalse(provider.getResourceByName("/directory").isPresent());
        Assert.assertTrue(provider.getResourceByName("/directory/index.html.md5").isPresent());
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
        Assert.assertTrue(provider.getResourceByName("/directory/other.html.md5").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        Assert.assertEquals(
                Sets.newHashSet("176b689259e8d68ef0aa869fd3b3be45", "0c84751f0ca9c6886bb09f2dd1a66faa"),
                provider.stream().map(castFunctionWithException(KuteIO::readResource)).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        Assert.assertEquals("176b689259e8d68ef0aa869fd3b3be45", readResource(provider.getResourceByName("/directory/index.html.md5").get()));
        Assert.assertEquals("0c84751f0ca9c6886bb09f2dd1a66faa", readResource(provider.getResourceByName("/directory/other.html.md5").get()));
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

class ChecksumMap implements FunctionWithException<Resource.Readable, Resource.Readable, IOException> {


    @Override
    public Resource.Readable applyWithException(Resource.Readable readable) {
        return Kute.stringResource(readable.getPath() + ".md5", Hex.encodeHexString(KuteDigest.md5(readable)));
    }


    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ChecksumMap;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChecksumMap{}";
    }
}