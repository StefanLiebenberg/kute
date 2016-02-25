package org.slieb.kute.providers;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.slieb.throwables.SupplierWithThrowable;
import org.slieb.kute.KuteIO;
import org.slieb.kute.api.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public class ZipStreamResourceProviderTest implements ProviderTestInterface {

    private ZipStreamResourceProvider provider;

    @Before
    public void setup() throws Exception {
        URL url = getClass().getResource("/resources-sample.zip");
        File file = new File(url.getFile());
        provider = new ZipStreamResourceProvider(new ZipSupplier(file));
    }

    @Test
    public void testHasResources() {
        assertTrue(provider.getResourceByName("/resource.txt").isPresent());
        assertTrue(provider.getResourceByName("/nested/resource.txt").isPresent());
        assertTrue(provider.getResourceByName("/nested/other.txt").isPresent());
        assertFalse(provider.getResourceByName("/nested/foo.txt").isPresent());
    }

    @Test
    public void testResourceTxt() throws IOException {
        Resource.Readable inputStreaming = provider.getResourceByName("/resource.txt").get();
        assertNotNull(inputStreaming);
        String expectedContent = "resource content for /resource.txt\n";
        assertEquals(expectedContent, KuteIO.readResourceWithInputStream(inputStreaming, "UTF-8"));
    }

    @Test
    public void testStream() throws IOException {
        assertEquals(newHashSet("/resource.txt", "/nested/resource.txt", "/nested/other.txt"),
                     provider.stream().map(Resource::getPath).collect(toSet()));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        assertTrue(provider.stream().noneMatch(resources -> resources.getPath().equals("/nested")));
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        assertFalse(provider.getResourceByName("/nested").isPresent());
    }

    @Override
    @Test
    public void shouldNeverReturnNullOnGetByPath() throws Exception {
        assertNotNull(provider.getResourceByName(""));
        assertNotNull(provider.getResourceByName("/none"));
    }

    @Override
    @Test
    public void shouldReturnElementsInStream() throws Exception {
        assertTrue(provider.stream().limit(1).count() > 0);
    }

    @Override
    @Test
    public void shouldReturnPresentOptionalInGetByPath() throws Exception {
        assertTrue(provider.getResourceByName("/nested/other.txt").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        assertEquals(
                Sets.newHashSet("resource content for /resource.txt\n", "resource content for /nested/resource.txt\n",
                                "resource content for /nested/other.txt\n"),
                provider.stream().map(KuteIO::readResourceUnsafe).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        assertEquals("resource content for /resource.txt\n", KuteIO.readResource(provider.getResourceByName("/resource.txt").get()));
        assertEquals("resource content for /nested/resource.txt\n", KuteIO.readResource(provider.getResourceByName("/nested/resource.txt").get()));
        assertEquals("resource content for /nested/other.txt\n", KuteIO.readResource(provider.getResourceByName("/nested/other.txt").get()));
    }

    @Override
    @Test
    public void shouldReturnAllResourcesInStreamInGetByPath() throws Exception {
        provider.stream().forEach(resource -> {
            assertEquals(Optional.of(resource), provider.getResourceByName(resource.getPath()));
        });
    }

//    @Override
//    @Test
//    public void shouldBeSerializable() throws Exception {
//        final ZipStreamResourceProvider loaded = KuteIO.deserialize(KuteIO.serialize(provider), provider.getClass());
//        assertEquals(provider, loaded);
//        assertEquals(provider.toString(), loaded.toString());
//        assertEquals(provider.hashCode(), loaded.hashCode());
//        assertTrue(provider.equals(loaded));
//    }
}

class ZipSupplier implements SupplierWithThrowable<ZipInputStream, IOException>, Serializable {

    private final File file;

    public ZipSupplier(File file) {
        this.file = file;
    }

    @Override
    public ZipInputStream getWithThrowable() throws IOException {
        return new ZipInputStream(new FileInputStream(file));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ZipSupplier)) { return false; }
        ZipSupplier that = (ZipSupplier) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return "ZipSupplier{" +
                "file=" + file +
                '}';
    }
}