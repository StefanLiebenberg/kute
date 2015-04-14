package slieb.kute.api;

import java.util.function.Supplier;

/**
 * <p>The resource provider supplies a {@link java.lang.Iterable} of {@link slieb.kute.api.Resource}.</p>
 * <p>An example would be a directory resource that supplies its contents as resource objects.</p>
 * <p>Its possible to compose resource providers in each other to get complex functionality, for example:</p>
 * <pre><code>
 *     ResourceProvider&lt;Resource.Readable&gt; resourceProvider = new FileResourceProvider(sourceDirectory);
 *     ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
 *     ResourceProvider&lt;Resource.Readable&gt; filtered = new FilteredResourceProvider(resourceProvider, filter);
 *
 *     for(Resource.Readable resource : filtered.getResources()) {
 *        // iterate through all .txt files in directory
 *         String path = resource.getPath();
 *         try(Reader reader = resource.getReader()) {
 *             ... // do stuff with reader.
 *         }
 *     }
 * </code></pre>
 *
 * @param <A> A instance of {@link slieb.kute.api.Resource}, usually {@link slieb.kute.api.Resource.Readable}
 */
public interface ResourceProvider<A extends Resource> extends Supplier<Iterable<A>> {

    A getResourceByName(String path);

    Iterable<A> getResources();

    @Override
    default Iterable<A> get() {
        return getResources();
    }
}
