package slieb.kute.resources.filters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.api.Resource;

import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class PatternFilterTest {

    @Mock
    private Resource mockResource;


    private PatternFilter patternFilter;

    @Before
    public void setUp() throws Exception {
        patternFilter = new PatternFilter(Pattern.compile(".*\\.java"));
    }

    @Test
    public void testMatchingPattern() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/file/path/file.java");
        Assert.assertTrue(patternFilter.accepts(mockResource));
    }

    @Test
    public void testAlmostMatchingPattern() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/file/path/file_java");
        Assert.assertFalse(patternFilter.accepts(mockResource));
    }

    @Test
    public void testNotMatchingPattern() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/file/path/file.class");
        Assert.assertFalse(patternFilter.accepts(mockResource));
    }
}