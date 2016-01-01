package slieb.kute.utils.interfaces;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourcePredicateWithIO<T extends Resource> extends Serializable {
    boolean testWithIO(T resource) throws IOException;
}
