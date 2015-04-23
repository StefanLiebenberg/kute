package slieb.kute.resources.implementations;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.resources.Resources;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.junit.Assert.assertEquals;
import static slieb.kute.resources.Resources.readResource;

/**
 * Created by stefan on 4/22/15.
 */
public class MemoryCacheResourceTest {

    @Test
    public void testThreadSafe() {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append(i).append(".");
        }
        builder.append("done");

        Resource.Readable readable = new CounterResource();
        Resource.Readable cached = new MemoryCacheResource(readable);

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

        Resource.Readable readable = new CounterResource();
        Resource.Readable cached = new MemoryCacheResource(readable);

        assertEquals("0", Resources.readResource(cached));
        assertEquals("0", Resources.readResource(cached));
        assertEquals("1", Resources.readResource(readable));
        assertEquals("2", Resources.readResource(readable));
        assertEquals("0", Resources.readResource(cached));


    }

}


class CounterResource implements Resource.Readable {

    int i = 0;

    @Override
    public Reader getReader() {
        return new StringReader(String.valueOf(i++));
    }

    @Override
    public String getPath() {
        return "/counter_resource";
    }
}