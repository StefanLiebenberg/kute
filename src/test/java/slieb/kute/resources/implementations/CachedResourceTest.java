package slieb.kute.resources.implementations;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.resources.CachedResource;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static slieb.kute.utils.KuteIO.readResource;


public class CachedResourceTest {

    @Test
    public void testThreadSafe() {
        
        Resource.Readable readable = getCounterResource();
        Resource.Readable cached = new CachedResource(readable);

        ThreadFactory factory = Executors.defaultThreadFactory();
        ImmutableList.Builder<Thread> threadsBuilder = new ImmutableList.Builder<>();
        for (int y = 0; y < 10; y++) {
            threadsBuilder.add(factory.newThread(() -> {
                try {
                    assertEquals("0", readResource(cached));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
        ImmutableList<Thread> threads = threadsBuilder.build();
        threads.forEach(Thread::start);

        while (threads.stream().anyMatch(Thread::isAlive)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

    }


    @Test
    public void testCache() throws Throwable {

        Resource.Readable readable = getCounterResource();
        Resource.Readable cached = new CachedResource(readable);

        assertEquals("0", readResource(cached));
        assertEquals("0", readResource(cached));
        assertEquals("1", readResource(readable));
        assertEquals("2", readResource(readable));
        assertEquals("0", readResource(cached));

    }

    private Resource.Readable getCounterResource() {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        return Kute.stringResource("/counter", () -> String.valueOf(atomicInteger.getAndIncrement()));
    }

}
