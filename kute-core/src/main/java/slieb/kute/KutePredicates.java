package slieb.kute;


import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.slieb.throwables.FunctionWithThrowable;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourcePredicate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

public class KutePredicates {


    /**
     * @param <A> A implementation of resource.
     * @return A nonNull predicate.
     */
    public static <A extends Resource> ResourcePredicate<A> nonNull() {
        return p -> p != null;
    }

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> ResourcePredicate<A> all(ResourcePredicate<A>... predicates) {
        return (A resource) -> stream(predicates).allMatch(p -> p.test(resource));
    }

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> ResourcePredicate<A> any(final ResourcePredicate<A>... predicates) {
        return (A resource) -> stream(predicates).anyMatch(p -> p.test(resource));
    }

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> ResourcePredicate<A> none(ResourcePredicate<A>... predicates) {
        return (A resource) -> stream(predicates).noneMatch(p -> p.test(resource));
    }

    /**
     * @param extensions A variable list of extension strings.
     * @return True resource path ends with any of the extension strings.
     */
    public static ResourcePredicate<Resource> extensionFilter(String... extensions) {
        return (resource) -> KutePredicates.resourceHasExtension(resource, extensions);
    }

    /**
     * @param pattern A Pattern to match against the resource path
     * @return true if the resource path matches the specified pattern.
     */
    public static ResourcePredicate<Resource> patternFilter(Pattern pattern) {
        return (r) -> pattern.matcher(r.getPath()).matches();
    }

    /**
     * @param pattern A string Pattern to match against the resource path
     * @return true if the pattern matches the resource path.
     */
    public static ResourcePredicate<Resource> patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }

    /**
     * A predicate to filter out already seen resources by function.
     *
     * @param function The function to determine the resource value.
     * @param <R>      The Resource implementation.
     * @param <X>      The Value type.
     * @return A stateful predicate.
     */
    public static <R extends Resource, X> ResourcePredicate<R> distinctFilter(FunctionWithThrowable<R, X, IOException> function) {
        final Map<X, Boolean> seen = Maps.newConcurrentMap();
        return resource -> seen.putIfAbsent(function.apply(resource), Boolean.TRUE) == null;
    }


    /**
     * @param a         A resource to compare
     * @param resourceB A resource to compare
     * @return True if both resources have the same path
     */
    public static boolean resourcePathEquals(Resource a, Resource resourceB) {
        return Objects.equals(a.getPath(), resourceB.getPath());
    }

    /**
     * @param resourceA Readable resource
     * @param resourceB Readable resource
     * @return True if both resources have the same content
     * @throws IOException throws an ioException
     */
    public static boolean resourceContentEquals(Resource.Readable resourceA, Resource.Readable resourceB) throws IOException {
        try (InputStream inputStreamA = resourceA.getInputStream();
             InputStream inputStreamB = resourceB.getInputStream()) {
            return IOUtils.contentEquals(inputStreamA, inputStreamB);
        }
    }

    /**
     * @param resourceA Readable resource
     * @param resourceB Readable resource
     * @return True if both resources have the same path and content
     * @throws IOException throws an ioException
     */
    public static boolean resourceEquals(Resource.Readable resourceA, Resource.Readable resourceB) throws IOException {
        return resourcePathEquals(resourceA, resourceB) && resourceContentEquals(resourceA, resourceB);
    }


    /**
     * @param resource   A resource
     * @param extensions An array of extensions to check
     * @return Returns true if resource has given extension.
     */
    public static boolean resourceHasExtension(Resource resource, String... extensions) {
        return Arrays.stream(extensions).anyMatch(resource.getPath()::endsWith);
    }


}
