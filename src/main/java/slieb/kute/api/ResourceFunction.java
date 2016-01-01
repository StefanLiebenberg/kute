package slieb.kute.api;

import slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ResourceFunction<A extends Resource, B> extends Function<A, B>, Serializable {

}
