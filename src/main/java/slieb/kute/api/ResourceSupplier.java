package slieb.kute.api;

import slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.function.Supplier;

@FunctionalInterface
public interface ResourceSupplier<T extends Resource> extends Supplier<T>, Serializable {
}
