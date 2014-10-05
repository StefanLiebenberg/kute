package slieb.kute.resources.filters;

import slieb.kute.resources.Resource;
import slieb.kute.resources.ResourceFilter;

public class NegatedFilter implements ResourceFilter {
    private final ResourceFilter filter;

    public NegatedFilter(ResourceFilter filter) {
        this.filter = filter;
    }

    @Override
    public Boolean accepts(Resource resource) {
        return !this.filter.accepts(resource);
    }
}
