package slieb.kute;

import org.junit.Test;

import java.io.IOException;


public class KuteLambdasSafetyTest {

    @Test(expected = RuntimeException.class)
    public void testSafelyConsume() throws Exception {
        KuteLambdas.unsafeConsumer((object) -> {
            throw new IOException("expected io");
        }).accept(Kute.resource("/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyMapWithIO() throws Exception {
        KuteLambdas.unsafeMap((object) -> {
            throw new IOException("expected io");
        }).apply(Kute.resource("/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelySupply() throws Exception {
        KuteLambdas.unsafeSupply(() -> {
            throw new IOException("expected io");
        }).get();
    }
}