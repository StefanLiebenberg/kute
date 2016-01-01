package slieb.kute.api;

@FunctionalInterface
public interface ResourceConsumerWithIO<T extends Resource> extends ConsumerWithIO<T> {
}
