kute
====

kute provides a way to get resource content from different locations in the java/maven environment.

### Maven Dependency info

        <dependency>
            <groupId>org.slieb</groupId>
            <artifactId>kute</artifactId>
            <version>1.0</version>
        </dependency>

# Description

Kute is a resource library that provides some basic interfaces and common-usage implementations of them. 

  
| Interface             | Description                               | Methods                                       |
|-----------------------|-------------------------------------------|-----------------------------------------------|
| Resource              | Base resource interface                   | getPath()                                     |
| Resource.Readable     | Represents readable resources             | getReader(), getInputStream()                 | 
| Resource.Writable     | Represents writable resources             | getWriter(), getOutputStream()                |
| Resource.Provider     | A container for readable resources        | stream(), iterator(), getResourceByPath(path) |
| Resource.Creator      | A factory that creates writable resources | create(path)                                  |
| Resource.Checksumable | A resource that can be checksumed         | updateDigest()                                |

## Resource

Conceptually, a resource in Kute exists on some path. Therefore, the base resource interface implements 
only a getPath() method that returns a string.  

## Resource.Readable

The Readable interface extends the base Resource, and provides a getReader() and getInputStream() methods 
for read access to a given resource.

For example:

    File file = new File("custom.txt");
    FileResource fileResource = new FileResource(file); // FileResource implements Resource.Writable
    
    
    // query the input stream, and read directly from that.
    try(InputStream inputStream : fileResource.getInputStream()) {
      // do inputstream stuff
    }
        
    // or use a lambda let kute deal with ensuring that the stream is closed.
    fileResource.useInputStream(inputStream -> {
       // do inputstream stuff
    });
    
    // using a reader instead of the InputStream, the charset used depends on the implementing method.
    try(Reader reader : fileResource.getReader()) {
       // do reader stuff.
    }

    // or use a lambda and let kute deal with closing the stream.
    reader.useReader(reader -> {
      // do reader stuff
    });
    
    // or let kute deal with everthing, and just read the resource. See KuteIO for more useful methods.
    String fileContent = KuteIO.readResource(fileResource);
   

## Resource.Writable


The Readable interface extends the base Resource, and provides a getWriter() and getOutputStream() methods 
for write access to a given resource.
 
 
    File file = new File("custom.txt");
    FileResource fileResource = new FileResource(file); // FileResource implements Resource.Writable
    
    // query the output stream, and write directly into it
    try(OutputStream outputStream : fileResource.getOutputStream()) {
      // do outputStream stuff
    }
        
    // or use a lambda let kute deal with ensuring that the stream is closed.
    fileResource.useOutputStream(outputStream -> {
      // do outputStream stuff
    });
    
    // using a writer instead of the OutputStream, the charset used depends on the implementing method.
    try(Writer writer : fileResource.getWriter()) {
       // do reader stuff.
    }

    // or use a lambda and let kute deal with closing the stream.
    reader.useWriter(writer -> {
      // do reader stuff
    });
    
    // or let kute deal with everything, and just write to the resource. See KuteIO for more useful methods.
    KuteIO.writeResource(fileResource, "some content....");
    
## Resource.Provider

A Resource.Provider provides a stream of Resource.Readable instances via the the `stream()` method.       

# Examples

The Resource.Provider class implements Iterable and exposes a stream() method. This allows for easy management of resources.
 
### Getting default resource providers

Getting the default provider

    Resource.Provider provider = Kute.getDefaultProvider();
    
Getting a provider for specific class loader:
 
    Resource.Provider provider = Kute.getProvider(getClass().getClassLoader())
        

### Creating a ResourceProvider that provides files in a directory.

    File directory = ...;
    Resource.Provider provider = Kute.provideFrom(directory);
    for(FileResource resource : provider) {
        ... // get files inside directory as readable resources.
    }

### Create a ResourceProvider that provides elements from a url classLoader:

    URLClassLoader urlClassloader = (URLClassLoader) getClass().getClassLoader();
    Resource.Provider provider = new URLClassLoaderResourceProvider(urlClassloader);
    
### Create a ResourceProvider that acts as a filter on top of another provider.

    Resource.Provider provider = Kute.getDefaultProvider();
    ResourcePredicate<Resource> extFilter = KuteLambdas.extensionFilters(".txt", ".class");
    Resource.Provider filtered = Kute.filterResources(provider, extFilter)
    for(R resource : provider) {
        ... // only .txt or .class resources 
    }
    
### Use Java 8 lambda's on Resource Provider

    Resource.Provider provider = Kute.getDefaultProvider();
    provider.stream().forEach(resource -> {
       
    });


### Reading a Resource.Readable with Reader

    Optional<Resource.Readable> readable = resourceProvider.getResourceByName("/some/path.txt");
    if(readable.isPresent()) {
      try(Reader reader = readable.getReader()) {
         ... // do reading stuff with reader;
      }
    }
 
### Reading a Resource.Readable with Resources utility method.

    String content = KuteIO.readResource(resource);
    
### Writing to Resource.Writable

    Resource.Writable writable = ...;
    try(Writer writer = resource.getWriter()){ 
       ... // do stuff with writer;
    }
    
### Writing to Resource.Writable with Resources

    KuteIO.writeResource(readable, "content");
