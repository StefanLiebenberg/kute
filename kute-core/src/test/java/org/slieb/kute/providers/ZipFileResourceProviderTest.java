package org.slieb.kute.providers;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slieb.kute.KuteIO;
import org.slieb.kute.api.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.zip.ZipFile;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static org.slieb.kute.KuteIO.readResource;
import static org.slieb.kute.KuteIO.readResourceWithInputStream;
import static org.slieb.throwables.FunctionWithThrowable.castFunctionWithThrowable;

public class ZipFileResourceProviderTest implements ProviderTestInterface {

    private ZipFileResourceProvider provider;

    @Before
    public void setup() throws Exception {
        URL url = getClass().getResource("/resources-sample.zip");
        File file = new File(url.getFile());
        ZipFile zipFile = new ZipFile(file);
        provider = new ZipFileResourceProvider(zipFile);
    }

    @Test
    public void testHasResources() {
        assertNotNull(provider.getResourceByName("/resource.txt"));
        assertNotNull(provider.getResourceByName("/nested/resource.txt"));
        assertNotNull(provider.getResourceByName("/nested/other.txt"));
        assertFalse(provider.getResourceByName("/nested/foo.txt").isPresent());
    }

    @Test
    public void testResourceTxt() throws IOException {
        Resource.Readable inputStreaming = provider.getResourceByName("/resource.txt").get();
        assertNotNull(inputStreaming);
        String expectedContent = "resource content for /resource.txt\n";
        assertEquals(expectedContent, readResource(inputStreaming));
        assertEquals(expectedContent, readResourceWithInputStream(inputStreaming));
        assertEquals(expectedContent, readResourceWithInputStream(inputStreaming, "UTF-8"));
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
                provider.stream().map(castFunctionWithThrowable(KuteIO::readResource)).collect(toSet()));
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
    @Ignore
    public void shouldReturnAllResourcesInStreamInGetByPath() throws Exception {
        provider.stream().forEach(resource -> {
            assertEquals(Optional.of(resource), provider.getResourceByName(resource.getPath()));
        });
    }
}