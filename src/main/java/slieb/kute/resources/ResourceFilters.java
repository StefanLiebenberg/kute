package slieb.kute.resources;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class ResourceFilters {

    /**
     * Returns true for a given resource if either left or right returns true for the same resource.
     *
     * @param left  A resource filter
     * @param right A resource filter
     * @return A boolean value
     */
    public static ResourceFilter or(ResourceFilter left, ResourceFilter right) {
        return r -> left.accepts(r) || right.accepts(r);
    }

    /**
     * Returns true for a given resource if both left and right filters return true for the resource.
     *
     * @param left  A resource filter.
     * @param right A resource filter.
     * @return A boolean value.
     */
    public static ResourceFilter and(ResourceFilter left, ResourceFilter right) {
        return r -> left.accepts(r) && right.accepts(r);
    }

    /**
     * @param first   A resource filter.
     * @param filters A variable amount of resource filters.
     * @return A resource filter that returns true of all of filters return true.
     */
    public static ResourceFilter all(ResourceFilter first, ResourceFilter... filters) {
        return asList(filters).stream().reduce(first, ResourceFilters::and);
    }

    /**
     * @param first   A resource filter.
     * @param filters A variable amount of resource filters.
     * @return A resource filter that returns true of none of filters return true.
     */
    public static ResourceFilter none(ResourceFilter first, ResourceFilter... filters) {
        return not(any(first, filters));
    }

    /**
     * @param first   A resource filter.
     * @param filters A variable amount of resource filters.
     * @return A resource filter that returns true if any of filters return true.
     */
    public static ResourceFilter any(ResourceFilter first, ResourceFilter... filters) {
        return asList(filters).stream().reduce(first, ResourceFilters::or);
    }

    /**
     * @param filter A resource filter
     * @return A resource filter that is the negation of filter.
     */
    public static ResourceFilter not(ResourceFilter filter) {
        return resource -> !filter.accepts(resource);
    }

    /**
     * @param extensions A variable list of extension strings.
     * @return True resource path ends with any of the extension strings.
     */
    public static ResourceFilter extensionFilter(String... extensions) {
        return (r) -> asList(extensions).stream().anyMatch(r.getPath()::endsWith);
    }

    /**
     * @param pattern A Pattern to match against the resource path
     * @return true if the resource path matches the specified pattern.
     */
    public static ResourceFilter patternFilter(Pattern pattern) {
        return (r) -> pattern.matcher(r.getPath()).matches();
    }

    /**
     * @param pattern A string Pattern to match against the resource path
     * @return true if the pattern matches the resource path.
     */
    public static ResourceFilter patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }

    /**
     * @param predicate A predicate to turn into a filter.
     * @return A Resource filter.
     */
    public static ResourceFilter filter(Predicate<Resource> predicate) {
        return (ResourceFilter) predicate;
    }

}
