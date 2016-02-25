package org.slieb.kute.providers;

import com.google.common.collect.Sets;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slieb.kute.Kute;
import org.slieb.kute.KuteDigest;
import org.slieb.kute.KuteIO;
import org.slieb.throwables.FunctionWithThrowable;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.slieb.kute.KuteIO.readResource;
import static org.slieb.kute.KutePredicates.resourceEquals;
import static org.slieb.throwables.ConsumerWithThrowable.castConsumerWithThrowable;
import static org.slieb.throwables.FunctionWithThrowable.castFunctionWithThrowable;

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

    private void createContent(String path,
                               String content) throws Exception {
        KuteIO.writeResource(rawProvider.create(path), content);
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        Assert.assertFalse(provider.stream().anyMatch(resource -> resource.getPath().equals("/directory")));
        assertTrue(provider.stream().anyMatch(resource -> resource.getPath().startsWith("/directory")));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        Assert.assertFalse(provider.getResourceByName("/directory").isPresent());
        assertTrue(provider.getResourceByName("/directory/index.html.md5").isPresent());
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
        assertEquals(2, provider.stream().count());
    }

    @Override
    @Test
    public void shouldReturnPresentOptionalInGetByPath() throws Exception {
        assertTrue(provider.getResourceByName("/directory/other.html.md5").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        assertEquals(
                Sets.newHashSet("176b689259e8d68ef0aa869fd3b3be45", "0c84751f0ca9c6886bb09f2dd1a66faa"),
                provider.stream().map(castFunctionWithThrowable(KuteIO::readResource)).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        assertEquals("176b689259e8d68ef0aa869fd3b3be45", readResource(provider.getResourceByName("/directory/index.html.md5").get()));
        assertEquals("0c84751f0ca9c6886bb09f2dd1a66faa", readResource(provider.getResourceByName("/directory/other.html.md5").get()));
    }

    @Override
    @Test
    public void shouldReturnAllResourcesInStreamInGetByPath() throws Exception {
        assertEquals(2, provider.stream().count());
        provider.stream().forEach(castConsumerWithThrowable(readable -> {
            final Optional<Resource.Readable> optional = provider.getResourceByName(readable.getPath());
            assertEquals(Optional.of(readable), optional);
            assertTrue(resourceEquals(readable, optional.get()));
        }));
    }
}

class ChecksumMap implements FunctionWithThrowable<Resource.Readable, Resource.Readable, IOException> {

    @Override
    public Resource.Readable applyWithThrowable(Resource.Readable readable) {
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