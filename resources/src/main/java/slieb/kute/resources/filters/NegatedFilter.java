package slieb.kute.resources.filters;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

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
