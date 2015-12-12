package slieb.kute.api;


public interface ResourceCreator<A extends Resource.Writable> {
    A create(String path);
}
