package org.slieb.kute.api;

import org.junit.Assert;
import org.junit.Test;

public class ResourceTest {

    @Test
    public void testGetPath() {
        Resource resource = () -> TestUtils.EXPECTED_PATH;
        Assert.assertEquals(TestUtils.EXPECTED_PATH, resource.getPath());
    }

    @Test
    public void testGetPathX() {
        Resource resourceA = () -> "/pathA";
        Resource resourceB = () -> "/pathB";
        Assert.assertEquals(-1, resourceA.compareTo(resourceB));
        Assert.assertEquals(1, resourceB.compareTo(resourceA));
        Assert.assertEquals(0, resourceB.compareTo(resourceB));
        Assert.assertEquals(0, resourceA.compareTo(resourceA));
    }
}