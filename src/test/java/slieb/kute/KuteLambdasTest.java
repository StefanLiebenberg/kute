package slieb.kute;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.api.Resource;
import slieb.kute.utils.KuteLambdas;
import slieb.kute.utils.interfaces.ResourcePredicate;


@RunWith(MockitoJUnitRunner.class)
public class KuteLambdasTest {

    @Mock
    ResourcePredicate<Resource> filterT, filterF, filterM;

    @Mock
    Resource.Readable resourceA, resourceB;

    @Before
    public void setup() {
        Mockito.when(filterT.test(resourceA)).thenReturn(true);
        Mockito.when(filterT.test(resourceB)).thenReturn(true);

        Mockito.when(filterF.test(resourceA)).thenReturn(false);
        Mockito.when(filterF.test(resourceB)).thenReturn(false);

        Mockito.when(filterM.test(resourceA)).thenReturn(true);
        Mockito.when(filterM.test(resourceB)).thenReturn(false);


    }


    @Test
    public void testExtensionFilter() throws Exception {
        Mockito.when(resourceA.getPath()).thenReturn("/resourceA.txt");
        Mockito.when(resourceB.getPath()).thenReturn("/resourceB.log");

        Assert.assertTrue(KuteLambdas.extensionFilter(".txt").test(resourceA));
        Assert.assertFalse(KuteLambdas.extensionFilter(".txt").test(resourceB));
    }

    @Test
    public void testPatternFilter() throws Exception {
        Mockito.when(resourceA.getPath()).thenReturn("/aaaab");
        Mockito.when(resourceB.getPath()).thenReturn("/aaababc");

        Assert.assertTrue(KuteLambdas.patternFilter("/(a|b)*").test(resourceA));
        Assert.assertFalse(KuteLambdas.patternFilter("(a|b)*").test(resourceB));
    }


}