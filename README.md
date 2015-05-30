kute
====

kute provides a way to get resource content from different locations in the java/maven environment.

# Examples


## ResourceProvider

The ResourceProvider class implements Iterable and exposes a stream() method. This allows for easy management of resources.
 
### Getting default resource providers

Getting the default provider

    ResourceProvider<Resource.InputStreaming> readableProvider = Kute.getDefaultProvider();
    
Getting a provider for specific class loader:
 
    ResourceProvider<Resource.InputStreaming> readableProvider = Kute.getProvider(getClass().getClassLoader())
        

### Creating a ResourceProvider that provides files in a directory.

    File directory = ...;
    ResourceProvider<? extends Resource.Readable> provider = new FileResourceProvider(directory);
    for(Resource.Readable resource : provider) {
        ... // get files inside directory as readable resources.
    }

### Create a ResourceProvider that provides elements from a classLoader:

    ResourceProvider<Resource.Readable> provider = new URLClassLoaderResourceProvider((URLClassLoader) getClass().getClassLoader());
    
### Create a ResourceProvider that acts as a filter on top of another provider.

    ResourceProvider<Resource.InputStreaming> provider = Kute.getDefaultProvider();
    ResourceProvider<Resource.InputStreaming> filtered = Kute.filter(provider, ResourceFilters.extensionFilters(".txt", ".class"))
    for(R resource : provider) {
        ... // only .txt or .class resources 
    }
    
### Use Java 8 stream on Resource Providers

    ResourceProvider<Resource.InputStreaming> provider = Kute.getDefaultProvider();
    provider.stream().forEach(resource -> {
       
    });
    

## Resource

The Resource interface only has one method, getPath(). All implementations of resource will use this to indicate its 
location on the resource provider.  Furthermore, resource can implement one of four interfaces: 
Resource.Reader, Resource.Writer, Resource.InputStreaming or Resource.OutputStreaming.
 
Each of these have a relevant getReader, getWriter, etc method, which you can use to access or manipulate the git content of said resource.



### Reading a Resource.Readable with Reader

    Resource.Readable readable = resourceProvider.getResourceByName("/some/path.txt");
    try(Reader reader = readable.getReader()) {
       ... // do reading stuff with reader;
     }
 
### Reading a Resource.Readable with Resources

    String content = Kute.readResource(resource);
    
### Writing to Resource.Writable

    Resource.Writeable writeable = ...;
    try(Writer writer = resource.getWriter()){ 
       ... // do stuff with writer;
    }
    
### Writing to Resource.Writeable with Resources

    Kute.writeResource(readable, "content");


