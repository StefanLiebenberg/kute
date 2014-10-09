package slieb.kute.resources;

import slieb.kute.api.ResourceFilter;
import slieb.kute.resources.filters.*;

import java.util.regex.Pattern;

public class ResourceFilters {

    private ResourceFilters() {
    }

    public static AllFilter allFilter(ResourceFilter... filters) {
        return new AllFilter(filters);
    }

    public static AnyFilter anyFilter(ResourceFilter... filters) {
        return new AnyFilter(filters);
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

    public static NegatedFilter negatedFilter(ResourceFilter filter) {
        return new NegatedFilter(filter);
    }

}
