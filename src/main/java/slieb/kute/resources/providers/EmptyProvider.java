package slieb.kute.resources.providers;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.util.Optional;
import java.util.stream.Stream;

public class EmptyProvider<A extends Resource> implements ResourceProvider<A> {

    @Override
    public Optional<A> getResourceByName(String path) {
        return Optional.empty();
    }

    @Override
    public Stream<A> stream() {
        return Stream.empty();
    }
}
