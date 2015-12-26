package slieb.kute.providers;

import org.junit.Before;
import org.junit.Test;
import slieb.kute.utils.KuteIO;
import slieb.kute.api.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipInputStream;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;


public class ZipStreamResourceProviderTest {
    private ZipStreamResourceProvider provider;

    @Before
    public void setup() throws Exception {
        URL url = getClass().getResource("/resources-sample.zip");
        File file = new File(url.getFile());
        provider = new ZipStreamResourceProvider(() -> {
            try {
                return new ZipInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
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

}