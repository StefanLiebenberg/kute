package org.slieb.kute.resources;

import org.junit.Assert;
import org.junit.Test;

public class BytesArrayResourceTest {

    @Test
    public void testEqualsWithDifferentContent() {
        BytesArrayResource resourceA = new BytesArrayResource("/path", "text".getBytes());
        BytesArrayResource resourceB = new BytesArrayResource("/path", "textB".getBytes());
        Assert.assertNotEquals(resourceA, resourceB);
        Assert.assertNotEquals(resourceA.toString(), resourceB.toString());
    }

    @Test
    public void testEqualsWithIdenticalContent() {
        BytesArrayResource resourceA = new BytesArrayResource("/path", "text".getBytes());
        BytesArrayResource resourceB = new BytesArrayResource("/path", "text".getBytes());
        Assert.assertEquals(resourceA, resourceB);
        Assert.assertEquals(resourceA.toString(), resourceB.toString());
    }
}