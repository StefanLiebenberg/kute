package slieb.kute.resources.special;

import org.junit.Assert;
import org.junit.Test;
import slieb.kute.resources.Resource;
import slieb.kute.resources.api.URLResource;

import java.io.File;

public class LazyFileProviderTest {

    @Test
    public void testGetFile() throws Exception {
        String path = "/slieb/kute/resources/example.txt";
        Resource.Readable readable = new URLResource(getClass().getResource(path), path);
        LazyFileProvider lazyFileProvider = new LazyFileProvider(readable);
        File file = lazyFileProvider.getFile();
        Assert.assertTrue(file.exists());
    }
}