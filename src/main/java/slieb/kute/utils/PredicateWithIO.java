package slieb.kute.utils;

import java.io.IOException;

@FunctionalInterface
public interface PredicateWithIO<T> {

    boolean testWithIO(T object) throws IOException;
}
