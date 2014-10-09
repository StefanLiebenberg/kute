package slieb.kute.resources.filters;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

public class AnyFilter implements ResourceFilter {
    private final ResourceFilter[] resourceFilters;

    public AnyFilter(ResourceFilter... resourceFilters) {
        this.resourceFilters = resourceFilters;
    }

    @Override
    public Boolean accepts(Resource resource) {
        for (ResourceFilter filter : resourceFilters) {
            if (filter.accepts(resource)) {
                return true;
            }
        }
        return false;
    }
}
