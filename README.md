kute
====

kute provides a way to get resource content from different locations in the java/maven environment.

# Examples

## Resource

### Reading a Resource.Readable with Reader

    Resource.Readable readable = ...;
    try(Reader reader = readable.getReader()) {
       ... // do reading stuff with reader;
     }
 
### Reading a Resource.Readable with Resources

    Resources.Readable readable = ...;
    String content = Resources.readResource(readable);
    
### Writing to Resource.Writable

    Resource.Writeable writeable = ...;
    try(Writer writer = resource.getWriter()){ 
       ... // do stuff with writer;
    }
    
### Writing to Resource.Writeable with Resources

    String content = ...;
    Resources.Writeable readable = ...;
    Resources.writeResource(readable, content);


## ResourceProvider

The ResourceProvider class implements Iterable and exposes a stream() method. This allows for easy management of resources. 

### Create File resource Provider

    File directory = ...;
    ResourceProvider<? extends Resource.Readable> provider = new FileResourceProvider(directory);
    for(Resource.Readable resource : provider) {
        ... // get files inside directory as readable resources.
    }

### Get class path resource provider

    ResourceProvider<Resource.Readable> provider = new URLClassLoaderResourceProvider((URLClassLoader) getClass().getClassLoader());
    

### Filter Resource Provider by extension

    ResourceProvider<R> provider = ...; // R extends Resource, mostly Resource.Readable.
    ResourceFilter filter = new ExtensionFilter(".txt", ".class");
    ResourceProvider<R> filtered = new FilteredResourceProvider<R>(provider, filter);
    for(R resource : provider) {
        ... // only .txt or .class resources 
    }
    
### Use Java 8 stream on Resource Providers

    ResourceProvider<R> provider = Kute.getDefaultProvider();
    provider.stream().forEach(resource -> {
       
    });
    
