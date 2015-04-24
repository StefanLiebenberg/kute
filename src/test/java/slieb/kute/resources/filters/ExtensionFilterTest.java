package slieb.kute.resources.filters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.api.Resource;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static slieb.kute.resources.Resources.stringResource;

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
        when(mockResource.getPath()).thenReturn("/the/path/to.png");
        assertFalse(extensionFilter.accepts(mockResource));
    }

    @Test
    public void testFiltersResourceWithCorrectExtension() throws Exception {
        when(mockResource.getPath()).thenReturn("/the/path/to.class");
        assertTrue(extensionFilter.accepts(mockResource));
    }

    @Test
    public void testThreadSafe() throws Exception {
        range(0, 1000)
                .parallel()
                .forEach(i -> {
                    assertTrue(extensionFilter.accepts(stringResource("content", "/my/passwords.txt")));
                    assertTrue(extensionFilter.accepts(stringResource("content", "/my/Car.class")));
                    assertFalse(extensionFilter.accepts(stringResource("content", "/my/pictures/cat.png")));
                });
    }


}