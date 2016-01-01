package slieb.kute.utils.interfaces;

import slieb.kute.api.Resource;

import java.io.IOException;

@FunctionalInterface
public interface ResourceConsumerWithIO<T extends Resource> {
    void acceptWithIO(T object) throws IOException;
}
