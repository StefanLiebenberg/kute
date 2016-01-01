package slieb.kute;

import org.junit.Test;

import java.io.IOException;

import static org.slieb.throwables.ConsumerWithException.castConsumerWithException;
import static org.slieb.throwables.FunctionWithException.castFunctionWithException;
import static org.slieb.throwables.SupplierWithException.castSupplierWithException;


public class KuteLambdasSafetyTest {

    @Test(expected = RuntimeException.class)
    public void testSafelyConsume() throws Exception {
        castConsumerWithException((object) -> {
            throw new IOException("expected io");
        }).accept(Kute.resource("/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyMapWithIO() throws Exception {
        castFunctionWithException((object) -> {
            throw new IOException("expected io");
        }).apply(Kute.resource("/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelySupply() throws Exception {
        castSupplierWithException(() -> {
            throw new IOException("expected io");
        }).get();
    }
}