package slieb.kute.api;


public interface ResourceCreator<A extends Resource.Writeable> {
    A create(String path);
}
