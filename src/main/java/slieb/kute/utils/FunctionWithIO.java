package slieb.kute.utils;

import java.io.IOException;

@FunctionalInterface
public interface FunctionWithIO<A, B> {
    B applyWithIO(A object) throws IOException;
}
