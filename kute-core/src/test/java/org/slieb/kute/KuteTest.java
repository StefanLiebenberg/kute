package org.slieb.kute;

import org.junit.Test;
import org.slieb.kute.api.Resource;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.slieb.kute.Kute.getDefaultProvider;
import static org.slieb.kute.Kute.getProvider;
import static org.slieb.kute.KuteIO.readResource;
import static org.slieb.kute.KuteIO.readResourceWithInputStream;
import static org.slieb.throwables.IntConsumerWithThrowable.castIntConsumerWithThrowable;

public class KuteTest {

    public static final String SAMPLE_TEXT = "just contains example text.";
    public static final String EXAMPLE_PATH = "/org/slieb/kute/resources/example.txt";

    @Test
    public void testGetProvider() throws Exception {
        Resource.Provider provider = getProvider(getClass().getClassLoader());
        assertNotNull(provider);
        Resource.Readable readable = provider.getResourceByName(EXAMPLE_PATH).get();
        assertNotNull("readable cannot be null", readable);
        assertEquals(SAMPLE_TEXT, readResource(readable));
    }

    @Test
    public void testGetDefaultProvider() throws Exception {
        Resource.Provider readables = getDefaultProvider();
        assertNotNull(readables);
        Resource.Readable readable = readables.getResourceByName(EXAMPLE_PATH).get();
        assertNotNull(readable);
        assertEquals(SAMPLE_TEXT, readResourceWithInputStream(readable));
    }

    @Test
    public void testGetProviderIsThreadSafe() throws Exception {
        IntStream.range(0, 100).parallel().forEach(castIntConsumerWithThrowable(i -> {
            assertEquals(SAMPLE_TEXT, readResource(
                    getDefaultProvider().getResourceByName(EXAMPLE_PATH).get()));
        }));
    }

    @Test
    public void testGetEmptyProvider() {
        assertEquals(0, Kute.emptyProvider().stream().count());
    }
}