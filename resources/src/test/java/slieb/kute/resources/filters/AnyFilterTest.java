package slieb.kute.resources.filters;

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
public class AnyFilterTest {

    @Mock
    ResourceFilter mockFilterA, mockFilterB, mockFilterC;

    @Mock
    Resource mockResource;

    AnyFilter anyFilter;

    @Before
    public void setUp() throws Exception {
        anyFilter = new AnyFilter(mockFilterA, mockFilterB, mockFilterC);
    }

    @Test
    public void testAllFilterWhenOneIsTrue() throws Exception {
        Mockito.when(mockFilterA.accepts(mockResource)).thenReturn(true);
        Mockito.when(mockFilterB.accepts(mockResource)).thenReturn(false);
        Mockito.when(mockFilterC.accepts(mockResource)).thenReturn(false);
        Assert.assertTrue(anyFilter.accepts(mockResource));
    }

    @Test
    public void testAllFilterWhenOneIsFalse() throws Exception {
        Mockito.when(mockFilterA.accepts(mockResource)).thenReturn(false);
        Mockito.when(mockFilterB.accepts(mockResource)).thenReturn(true);
        Mockito.when(mockFilterC.accepts(mockResource)).thenReturn(true);

        Assert.assertTrue(anyFilter.accepts(mockResource));
    }

    @Test
    public void testAllFilterWhenAllIsTrue() throws Exception {
        Mockito.when(mockFilterA.accepts(mockResource)).thenReturn(true);
        Mockito.when(mockFilterB.accepts(mockResource)).thenReturn(true);
        Mockito.when(mockFilterC.accepts(mockResource)).thenReturn(true);

        Assert.assertTrue(anyFilter.accepts(mockResource));
    }

    @Test
    public void testAllFilterWhenAllIsFalse() throws Exception {
        Mockito.when(mockFilterA.accepts(mockResource)).thenReturn(false);
        Mockito.when(mockFilterB.accepts(mockResource)).thenReturn(false);
        Mockito.when(mockFilterC.accepts(mockResource)).thenReturn(false);

        Assert.assertFalse(anyFilter.accepts(mockResource));
    }
}