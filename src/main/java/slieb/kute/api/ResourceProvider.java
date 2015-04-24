package slieb.kute.api;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * <p>The resource provider supplies a {@link java.lang.Iterable} of {@link slieb.kute.api.Resource}.</p>
 * <p>An example would be a directory resource that supplies its contents as resource objects.</p>
 * <p>Its possible to compose resource providers in each other to get complex functionality, for example:</p>
 * <pre><code>
 *     ResourceProvider&lt;Resource.Readable&gt; resourceProvider = new FileResourceProvider(sourceDirectory);
 *     ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
 *     ResourceProvider&lt;Resource.Readable&gt; filtered = new FilteredResourceProvider(resourceProvider, filter);
 *     for(Resource.Readable resource : filtered) {
 *        // iterate through all .txt files in directory
 *         String path = resource.getPath();
 *         try(Reader reader = resource.getReader()) {
 *             ... // do stuff with reader.
 *         }
 *     }
 * </code></pre>
 * <p>Or using java 8 streams:</p>
 * <pre><code>
 *     ResourceProvider&lt;Resource.Readable&gt; resourceProvider = new FileResourceProvider(sourceDirectory);
 *     ResourceFilter filter = new PatternFilter(Pattern.compile(".*\\.txt"));
 *     ResourceProvider&lt;Resource.Readable&gt; filtered = new FilteredResourceProvider(resourceProvider, filter);
 *     filtered.stream().forEach( resource -&lt; {
 *        // iterate through all .txt files in directory
 *         String path = resource.getPath();
 *         try(Reader reader = resource.getReader()) {
 *             ... // do stuff with reader.
 *         }
 *     });
 * </code></pre>
 *
 * @param <A> A instance of {@link slieb.kute.api.Resource}, usually {@link slieb.kute.api.Resource.Readable}
 */
public interface ResourceProvider<A extends Resource> extends Iterable<A> {

    A getResourceByName(String path);

    @Override
    default Iterator<A> iterator() {
        return stream().iterator();
    }

    Stream<A> stream();
}
