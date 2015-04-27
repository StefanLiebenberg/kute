package slieb.kute;

import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static slieb.kute.Kute.getDefaultProvider;
import static slieb.kute.Kute.getProvider;
import static slieb.kute.resources.Resources.readResource;


public class KuteTest {

    @Test
    public void testGetProvider() throws Exception {
        ResourceProvider<Resource.Readable> readables = getProvider(getClass().getClassLoader());
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName("/slieb/kute/resources/example.txt");
        assertNotNull("readable cannot be null", readable);
        assertEquals("just contains example text.", readResource(readable));
    }

    @Test
    public void testGetDefaultProvider() throws Exception {
        ResourceProvider<Resource.Readable> readables = getDefaultProvider();
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName("/slieb/kute/resources/example.txt");
        assertNotNull(readable);
        assertEquals("just contains example text.", readResource(readable));
    }

    @Test
    public void testGetProviderIsThreadSafe() throws Exception {
        IntStream.range(0, 100)
                .parallel()
                .forEach(i -> {
                    try {
                        assertEquals("just contains example text.", readResource(getDefaultProvider().getResourceByName("/slieb/kute/resources/example.txt")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}