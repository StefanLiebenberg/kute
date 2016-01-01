package slieb.kute.api;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface SupplierWithIO<A> extends Serializable {

    A getWithIO() throws IOException;
}
