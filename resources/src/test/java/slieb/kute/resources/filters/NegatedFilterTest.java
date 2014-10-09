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
public class NegatedFilterTest {

    @Mock
    private ResourceFilter mockResourceFilter;

    @Mock
    private Resource mockResource;

    private NegatedFilter negatedFilter;

    @Before
    public void setUp() throws Exception {
        negatedFilter = new NegatedFilter(mockResourceFilter);
    }

    @Test
    public void testWhenFilterReturnsTrue() throws Exception {
        Mockito.when(mockResourceFilter.accepts(mockResource)).thenReturn(true);
        Assert.assertFalse(negatedFilter.accepts(mockResource));
    }

    @Test
    public void testWhenFilterReturnsFalse() throws Exception {
        Mockito.when(mockResourceFilter.accepts(mockResource)).thenReturn(false);
        Assert.assertTrue(negatedFilter.accepts(mockResource));
    }
}