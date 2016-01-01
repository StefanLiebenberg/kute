package slieb.kute;

import org.junit.Test;
import org.slieb.unnamed.api.SupplierWithException;

import java.io.IOException;

import static org.slieb.unnamed.api.ConsumerWithException.castConsumerWithException;
import static org.slieb.unnamed.api.FunctionWithException.castFunctionWithException;


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
        SupplierWithException.castSupplierWithException(() -> {
            throw new IOException("expected io");
        }).get();
    }
}