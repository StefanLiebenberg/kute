package slieb.kute;

import org.junit.Test;
import slieb.kute.utils.KuteLambdas;

import java.io.IOException;


public class KuteLambdasSafetyTest {

    @Test(expected = RuntimeException.class)
    public void testSafelyConsume() throws Exception {
        KuteLambdas.safelyConsume((object) -> {
            throw new IOException("expected io");
        }).accept(new Object());
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyMapWithIO() throws Exception {
        KuteLambdas.safelyMapWithIO((object) -> {
            throw new IOException("expected io");
        }).apply(new Object());
    }

    @Test(expected = RuntimeException.class)
    public void testSafelySupply() throws Exception {
        KuteLambdas.safelySupply(() -> {
            throw new IOException("expected io");
        }).get();
    }
}