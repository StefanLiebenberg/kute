package org.slieb.kute.providers;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slieb.kute.KuteIO;
import org.slieb.kute.api.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.slieb.throwables.FunctionWithThrowable.castFunctionWithThrowable;
import static org.slieb.kute.KuteIO.readResource;

public class DirectoryProviderTest implements ProviderTestInterface {

    private DirectoryProvider provider;

    private File tempDirectory;

    @Before
    public void setUp() throws Exception {
        tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();
        provider = new DirectoryProvider(tempDirectory);
        createContent("index content", "directory/index.html");
        createContent("other content", "directory/other.html");
    }

    private void createContent(String content,
                               String path) throws IOException {
        File resourceFile = new File(tempDirectory, path);
        resourceFile.deleteOnExit();
        Assert.assertTrue(resourceFile.getParentFile().exists() || resourceFile.getParentFile().mkdirs());
        try (FileWriter writer = new FileWriter(resourceFile)) {
            IOUtils.write(content, writer);
        }
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
        Assert.assertTrue(provider.getResourceByName("/directory/index.html").isPresent());
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
        Assert.assertTrue(provider.getResourceByName("/directory/other.html").isPresent());
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        Assert.assertEquals(
                Sets.newHashSet("index content", "other content"),
                provider.stream().map(castFunctionWithThrowable(KuteIO::readResource)).collect(toSet()));
    }

    @Override
    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        Assert.assertEquals("index content", readResource(provider.getResourceByName("/directory/index.html").get()));
        Assert.assertEquals("other content", readResource(provider.getResourceByName("/directory/other.html").get()));
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
}
