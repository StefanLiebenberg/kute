package slieb.kute;

import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static slieb.kute.Kute.*;


public class KuteTest {

    @Test
    public void testGetProvider() throws Exception {
        ResourceProvider<Resource.InputStreaming> readables = getProvider(getClass().getClassLoader());
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName("/slieb/kute/resources/example.txt");
        assertNotNull("readable cannot be null", readable);
        assertEquals("just contains example text.", readResource(readable));
    }

    @Test
    public void testGetDefaultProvider() throws Exception {
        ResourceProvider<Resource.InputStreaming> readables = getDefaultProvider();
        assertNotNull(readables);
        Resource.InputStreaming readable = readables.getResourceByName("/slieb/kute/resources/example.txt");
        assertNotNull(readable);
        assertEquals("just contains example text.", readStreamResource(readable));
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

    public void testGroup() throws Exception {
        ResourceProvider<? extends Resource.Readable> providerA, providerB, providerC;
        providerA = Kute.providerOf();
        providerB = Kute.providerOf();
        providerC = Kute.<Resource.Readable, ResourceProvider>group(providerA, providerA);
    }
}