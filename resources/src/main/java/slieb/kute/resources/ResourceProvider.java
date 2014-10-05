package slieb.kute.resources;

public interface ResourceProvider<A extends Resource> {
    
    public A getResourceByNamespace(String namespace);

    public Iterable<A> getResources();
}
