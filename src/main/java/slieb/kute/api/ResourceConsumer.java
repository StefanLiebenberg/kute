package slieb.kute.api;

import slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface ResourceConsumer<T extends Resource> extends Serializable, Consumer<T> {
    void accept(T object);
}
