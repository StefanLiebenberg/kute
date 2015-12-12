package slieb.kute.resources.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.resources.implementations.URLResource;

import static slieb.kute.Kute.readResource;

public class URLResourceTest {

    private static String PATH = "/slieb/kute/resources/example.txt";

    URLResource urlResource;

    @Before
    public void setUp() throws Exception {
        urlResource = new URLResource(PATH, getClass().getResource(PATH));
    }

    @Test
    public void testGetReader() throws Exception {
        Assert.assertEquals("just contains example text.", readResource(urlResource));
    }

    @Test
    public void testGetNamespace() throws Exception {
        Assert.assertEquals(PATH, urlResource.getPath());
    }

    @Test
    public void testEquals() throws Exception {
        URLResource sameResource = new URLResource(PATH, getClass().getResource(PATH));
        Assert.assertEquals(sameResource, urlResource);
    }

}