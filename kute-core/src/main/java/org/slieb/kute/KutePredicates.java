package org.slieb.kute;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.slieb.throwables.FunctionWithThrowable;
import org.slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

public class KutePredicates {

    /**
     * @return A nonNull predicate.
     */
    public static Resource.Predicate nonNull() {
        return p -> p != null;
    }

    /**
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    public static Resource.Predicate all(Resource.Predicate... predicates) {
        return (Resource resource) -> stream(predicates).allMatch(p -> p.test(resource));
    }

    /**
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    public static Resource.Predicate any(final Resource.Predicate... predicates) {
        return (Resource resource) -> stream(predicates).anyMatch(p -> p.test(resource));
    }

    /**
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    public static Resource.Predicate none(Resource.Predicate... predicates) {
        return (Resource resource) -> stream(predicates).noneMatch(p -> p.test(resource));
    }

    /**
     * @param extensions A variable list of extension strings.
     * @return True resource path ends with any of the extension strings.
     */
    public static Resource.Predicate extensionFilter(String... extensions) {
        return (resource) -> KutePredicates.resourceHasExtension(resource, extensions);
    }

    /**
     * @param pattern A Pattern to match against the resource path
     * @return true if the resource path matches the specified pattern.
     */
    public static Resource.Predicate patternFilter(Pattern pattern) {
        return (r) -> pattern.matcher(r.getPath()).matches();
    }

    /**
     * @param pattern A string Pattern to match against the resource path
     * @return true if the pattern matches the resource path.
     */
    public static Resource.Predicate patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }

    /**
     * A predicate to filter out already seen resources by function.
     *
     * @param function The function to determine the resource value.
     * @param <X>      The Value type.
     * @return A stateful predicate.
     */
    public static <X> Resource.Predicate distinctFilter(FunctionWithThrowable<Resource, X, IOException> function) {
        final Map<X, Boolean> seen = Maps.newConcurrentMap();
        return resource -> seen.putIfAbsent(function.apply(resource), Boolean.TRUE) == null;
    }

    /**
     * @param a         A resource to compare
     * @param resourceB A resource to compare
     * @return True if both resources have the same path
     */
    public static boolean resourcePathEquals(Resource a,
                                             Resource resourceB) {
        return Objects.equals(a.getPath(), resourceB.getPath());
    }

    /**
     * @param resourceA Readable resource
     * @param resourceB Readable resource
     * @return True if both resources have the same content
     * @throws IOException throws an ioException
     */
    public static boolean resourceContentEquals(Resource.Readable resourceA,
                                                Resource.Readable resourceB) throws IOException {
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
    public static boolean resourceEquals(Resource.Readable resourceA,
                                         Resource.Readable resourceB) throws IOException {
        return resourcePathEquals(resourceA, resourceB) && resourceContentEquals(resourceA, resourceB);
    }

    /**
     * @param resource   A resource
     * @param extensions An array of extensions to check
     * @return Returns true if resource has given extension.
     */
    public static boolean resourceHasExtension(Resource resource,
                                               String... extensions) {
        return Arrays.stream(extensions).anyMatch(resource.getPath()::endsWith);
    }

    /**
     * @param providerLeft  The left provider
     * @param providerRight The right provider
     * @return True if the two providers provide the same data set.
     * @throws IOException An io Exception.
     */
    public static boolean providerEquals(Resource.Provider providerLeft,
                                         Resource.Provider providerRight) throws IOException {

        if (providerLeft == null || providerRight == null) {
            return providerLeft == providerRight;
        }

        final Iterator<Resource.Readable> iteratorLeft = providerLeft.stream().sorted().iterator();
        final Iterator<Resource.Readable> iteratorRight = providerRight.stream().sorted().iterator();

        while (iteratorLeft.hasNext() && iteratorRight.hasNext()) {
            if (!resourceEquals(iteratorLeft.next(), iteratorRight.next())) {
                return false;
            }
        }

        return !iteratorLeft.hasNext() && !iteratorRight.hasNext();
    }
}
