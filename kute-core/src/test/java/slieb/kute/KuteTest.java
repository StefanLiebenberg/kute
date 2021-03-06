package slieb.kute;

import org.junit.Test;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static slieb.kute.Kute.getDefaultProvider;
import static slieb.kute.Kute.getProvider;
import static slieb.kute.KuteIO.readResource;
import static slieb.kute.KuteIO.readResourceWithInputStream;


public class KuteTest {

    @Test
    public void testGetProvider() throws Exception {
        Resource.Provider provider = getProvider(getClass().getClassLoader());
        assertNotNull(provider);
        Resource.Readable readable = provider.getResourceByName("/slieb/kute/resources/example.txt").get();
        assertNotNull("readable cannot be null", readable);
        assertEquals("just contains example text.", readResource(readable));
    }

    @Test
    public void testGetDefaultProvider() throws Exception {
        Resource.Provider readables = getDefaultProvider();
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName("/slieb/kute/resources/example.txt").get();
        assertNotNull(readable);
        assertEquals("just contains example text.", readResourceWithInputStream(readable));
    }

    @Test
    public void testGetProviderIsThreadSafe() throws Exception {
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                assertEquals("just contains example text.", readResource(
                        getDefaultProvider().getResourceByName("/slieb/kute/resources/example.txt").get()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    public void testGetEmptyProvider() {
        assertEquals(0, Kute.emptyProvider().stream().count());
    }


}