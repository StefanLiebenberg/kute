package slieb.kute.resources.filters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.api.Resource;

@RunWith(MockitoJUnitRunner.class)
public class ExtensionFilterTest {

    @Mock
    private Resource mockResource;

    private ExtensionFilter extensionFilter;

    @Before
    public void setUp() throws Exception {
        extensionFilter = new ExtensionFilter(".txt", ".class");
    }

    @Test
    public void testFiltersResourceWithWrongExtension() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/the/path/to.png");
        Assert.assertFalse(extensionFilter.accepts(mockResource));
    }

    @Test
    public void testFiltersResourceWithCorrectExtension() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/the/path/to.class");
        Assert.assertTrue(extensionFilter.accepts(mockResource));
    }

}