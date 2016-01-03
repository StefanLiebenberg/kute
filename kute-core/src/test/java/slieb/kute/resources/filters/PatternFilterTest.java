package slieb.kute.resources.filters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.KutePredicates;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourcePredicate;

import java.util.regex.Pattern;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static slieb.kute.Kute.stringResource;

@RunWith(MockitoJUnitRunner.class)
public class PatternFilterTest {

    @Mock
    private Resource mockResource;


    private ResourcePredicate<Resource> patternFilter;

    @Before
    public void setUp() throws Exception {
        patternFilter = KutePredicates.patternFilter(Pattern.compile(".*\\.java"));
    }

    @Test
    public void testMatchingPattern() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/file/path/file.java");
        Assert.assertTrue(patternFilter.test(mockResource));
    }

    @Test
    public void testAlmostMatchingPattern() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/file/path/file_java");
        Assert.assertFalse(patternFilter.test(mockResource));
    }

    @Test
    public void testNotMatchingPattern() throws Exception {
        Mockito.when(mockResource.getPath()).thenReturn("/file/path/file.class");
        Assert.assertFalse(patternFilter.test(mockResource));
    }

    @Test
    public void testThreadSafe() throws Exception {
        range(0, 1000)
                .parallel()
                .forEach(i -> {
                    assertTrue(patternFilter.test(stringResource("/my/Car.java", "content")));
                    assertFalse(patternFilter.test(stringResource("/my/passwords.txt", "content")));
                    assertFalse(patternFilter.test(stringResource("/my/pictures/cat.png", "content")));
                });
    }
}