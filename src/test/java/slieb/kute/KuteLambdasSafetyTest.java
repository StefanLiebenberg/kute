package slieb.kute;

import org.junit.Test;

import java.io.IOException;

import static org.slieb.throwables.ConsumerWithThrowable.castConsumerWithThrowable;
import static org.slieb.throwables.FunctionWithThrowable.castFunctionWithThrowable;
import static org.slieb.throwables.SupplierWithThrowable.castSupplierWithThrowable;


public class KuteLambdasSafetyTest {

    @Test(expected = RuntimeException.class)
    public void testSafelyConsume() throws Exception {
        castConsumerWithThrowable((object) -> {
            throw new IOException("expected io");
        }).accept(Kute.resource("/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyMapWithIO() throws Exception {
        castFunctionWithThrowable((object) -> {
            throw new IOException("expected io");
        }).apply(Kute.resource("/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelySupply() throws Exception {
        castSupplierWithThrowable(() -> {
            throw new IOException("expected io");
        }).get();
    }
}