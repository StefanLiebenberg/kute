package slieb.kute.resources.filters;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

import java.util.regex.Pattern;

public class PatternFilter implements ResourceFilter {
    private final Pattern pattern;

    public PatternFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Boolean accepts(Resource resource) {
        return pattern.matcher(resource.getPath()).matches();
    }
}
