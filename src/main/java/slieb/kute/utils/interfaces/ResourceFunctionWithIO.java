package slieb.kute.utils.interfaces;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceFunctionWithIO<A extends Resource, B> extends Serializable {
    B applyWithIO(A object) throws IOException;
}
