package slieb.kute.resources.providers;

import org.junit.Before;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.api.Resource.InputStreaming;
import slieb.kute.resources.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipFile;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

/**
 * Created by stefan on 5/17/15.
 */
public class ZipFileResourceProviderTest {

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
        assertNull(provider.getResourceByName("/nested/foo.txt"));
    }

    @Test
    public void testResourceTxt() throws IOException {
        InputStreaming inputStreaming = provider.getResourceByName("/resource.txt");
        assertNotNull(inputStreaming);
        String expectedContent = "resource content for /resource.txt\n";
        assertEquals(expectedContent, Resources.readResource(inputStreaming));
        assertEquals(expectedContent, Resources.readResourceUnsafe(inputStreaming));
        assertEquals(expectedContent, Resources.readStreamResource(inputStreaming));
        assertEquals(expectedContent, Resources.readStreamResource(inputStreaming, "UTF-8"));
    }

    @Test
    public void testStream() throws IOException {
        assertEquals(newHashSet("/resource.txt", "/nested/resource.txt", "/nested/other.txt"), provider.stream().map(Resource::getPath).collect(toSet()));
    }

}