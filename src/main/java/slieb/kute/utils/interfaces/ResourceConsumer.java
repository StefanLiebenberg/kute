package slieb.kute.utils.interfaces;

import slieb.kute.api.Resource;

import java.io.Serializable;

@FunctionalInterface
public interface ResourceConsumer<T extends Resource> extends Serializable {
    void accept(T object);
}
