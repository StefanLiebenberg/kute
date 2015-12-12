package slieb.kute.resources.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmptyProviderTest {

    EmptyProvider emptyProvider;

    @Before
    public void setUp() throws Exception {
        emptyProvider = new EmptyProvider();
    }

    @Test
    public void testGetEmptyStream() throws Exception {
        Assert.assertNotNull(emptyProvider.stream());
        Assert.assertFalse(emptyProvider.stream().findAny().isPresent());
    }

    @Test
    public void testGetResources() throws Exception {
        Assert.assertFalse(emptyProvider.getResourceByName("/").isPresent());
    }
}