package slieb.kute;

import org.junit.Ignore;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static slieb.kute.Kute.getDefaultProvider;
import static slieb.kute.Kute.getProvider;
import static slieb.kute.resources.Resources.readResource;


public class KuteTest {

    @Test
    @Ignore
    public void testGetProvider() throws Exception {
        ResourceProvider<Resource.Readable> readables = getProvider(getClass().getClassLoader());
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName("/slieb/kute/resources/example.txt");
        assertNotNull(readable);
        assertEquals("just contains example text.", readResource(readable));
    }

    @Test
    @Ignore
    public void testGetDefaultProvider() throws Exception {
        ResourceProvider<Resource.Readable> readables = getDefaultProvider();
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName("/slieb/kute/resources/example.txt");
        assertNotNull(readable);
        assertEquals("just contains example text.", readResource(readable));
    }

//    @Test
//    public void testGetProviderIsThreadSafe() throws Exception {
//        range(0, 100)
//                .parallel()
//                .forEach(i -> {
//                    try {
//                        assertEquals("just contains example text.", readResource(getDefaultProvider().getResourceByName("/slieb/kute/resources/example.txt")));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//    }
}