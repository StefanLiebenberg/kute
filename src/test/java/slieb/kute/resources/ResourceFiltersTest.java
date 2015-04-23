package slieb.kute.resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

@RunWith(MockitoJUnitRunner.class)
public class ResourceFiltersTest {

    @Mock
    ResourceFilter filterT, filterF, filterM;

    @Mock
    Resource resourceA, resourceB;

    @Before
    public void setup() {
        Mockito.when(filterT.accepts(resourceA)).thenReturn(true);
        Mockito.when(filterT.accepts(resourceB)).thenReturn(true);

        Mockito.when(filterF.accepts(resourceA)).thenReturn(false);
        Mockito.when(filterF.accepts(resourceB)).thenReturn(false);

        Mockito.when(filterM.accepts(resourceA)).thenReturn(true);
        Mockito.when(filterM.accepts(resourceB)).thenReturn(false);

    }


    @Test
    public void testOr() throws Exception {
        Assert.assertTrue(ResourceFilters.or(filterT, filterM).accepts(resourceA));
        Assert.assertTrue(ResourceFilters.or(filterT, filterM).accepts(resourceB));
        Assert.assertTrue(ResourceFilters.or(filterF, filterM).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.or(filterF, filterM).accepts(resourceB));
        Assert.assertTrue(ResourceFilters.or(filterF, filterM).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.or(filterF, filterM).accepts(resourceB));
    }

    @Test
    public void testAnd() throws Exception {
        Assert.assertTrue(ResourceFilters.and(filterT, filterM).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.and(filterT, filterM).accepts(resourceB));
        Assert.assertFalse(ResourceFilters.and(filterF, filterM).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.and(filterF, filterM).accepts(resourceB));
        Assert.assertFalse(ResourceFilters.and(filterF, filterM).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.and(filterF, filterM).accepts(resourceB));
    }

    @Test
    public void testAll() throws Exception {
        Assert.assertFalse(ResourceFilters.all(filterT, filterT, filterF).accepts(resourceA));
        Assert.assertTrue(ResourceFilters.all(filterT, filterT, filterT).accepts(resourceA));
    }

    @Test
    public void testAny() throws Exception {
        Assert.assertTrue(ResourceFilters.any(filterT, filterF, filterF).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.any(filterF, filterF, filterF).accepts(resourceA));
    }

    @Test
    public void testNot() throws Exception {
        Assert.assertTrue(ResourceFilters.not(filterF).accepts(resourceA));
        Assert.assertFalse(ResourceFilters.not(filterT).accepts(resourceA));
    }

    @Test
    public void testExtensionFilter() throws Exception {
        Mockito.when(resourceA.getPath()).thenReturn("/resourceA.txt");
        Mockito.when(resourceB.getPath()).thenReturn("/resourceB.log");

        Assert.assertTrue(ResourceFilters.extensionFilter(".txt").accepts(resourceA));
        Assert.assertFalse(ResourceFilters.extensionFilter(".txt").accepts(resourceB));
    }

    @Test
    public void testPatternFilter() throws Exception {
        Mockito.when(resourceA.getPath()).thenReturn("/aaaab");
        Mockito.when(resourceB.getPath()).thenReturn("/aaababc");

        Assert.assertTrue(ResourceFilters.patternFilter("/(a|b)*").accepts(resourceA));
        Assert.assertFalse(ResourceFilters.patternFilter("(a|b)*").accepts(resourceB));
    }


}