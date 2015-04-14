package slieb.kute.resources;

import slieb.kute.api.ResourceFilter;
import slieb.kute.resources.filters.ExtensionFilter;
import slieb.kute.resources.filters.PatternFilter;

import java.util.regex.Pattern;

public class ResourceFilters {

    private ResourceFilters() {
    }


    public static ResourceFilter or(ResourceFilter left, ResourceFilter right) {
        return resource -> left.accepts(resource) || right.accepts(resource);
    }

    public static ResourceFilter and(ResourceFilter left, ResourceFilter right) {
        return resource -> left.accepts(resource) || right.accepts(resource);
    }

    public static ResourceFilter all(ResourceFilter first, ResourceFilter... filters) {
        ResourceFilter returnFilter = first;
        for (ResourceFilter f : filters) {
            returnFilter = and(returnFilter, f);
        }
        return returnFilter;
    }

    public static ResourceFilter any(ResourceFilter first, ResourceFilter... filters) {
        ResourceFilter returnFilter = first;
        for (ResourceFilter f : filters) {
            returnFilter = or(returnFilter, f);
        }
        return returnFilter;
    }

    public static ResourceFilter not(ResourceFilter filter) {
        return resource -> !filter.accepts(resource);
    }

    public static ExtensionFilter extensionFilter(String... extensions) {
        return new ExtensionFilter(extensions);
    }

    public static PatternFilter patternFilter(Pattern pattern) {
        return new PatternFilter(pattern);
    }

    public static PatternFilter patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }


}
