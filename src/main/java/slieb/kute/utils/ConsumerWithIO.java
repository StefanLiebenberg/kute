package slieb.kute.utils;

import java.io.IOException;

@FunctionalInterface
public interface ConsumerWithIO<T> {
    void acceptWithIO(T object) throws IOException;
}
