package slieb.kute.api;

/**
 * <p>The resource provider supplies a {@link java.lang.Iterable} of {@link slieb.kute.resources.Resource}.</p>
 * <p/>
 * <p>An example would be a directory resource that supplies its contents as resource objects.</p>
 * <p/>
 * <p>Its possible to compose resource providers in each other to get complex functionality, for example:</p>
 * <p/>
 * <pre><code>
 *     ResourceProvider<Resource.Readable> resourceProvider = new FileResourceProvider(sourceDirectory);
 *     ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
 *     ResourceProvider<Resource.Readable> filtered = new FilteredResourceProvider(resourceProvider, filter);
 * <p/>
 *     for(Resource.Readable resource : filtered.getResources()) {
 *        // iterate through all .txt files in directory
 *         String path = resource.getPath();
 *         try(Reader reader = resource.getReader()) {
 *             ... // do stuff with reader.
 *         }
 *     }
 * </code></pre>
 *
 * @param <A> A instance of {@link slieb.kute.resources.Resource}, usually {@link slieb.kute.resources.Resource.Readable}
 */
public interface ResourceProvider<A extends Resource> {

    public A getResourceByName(String path);

    public Iterable<A> getResources();
}
