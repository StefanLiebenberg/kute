package slieb.kute.resources.filters;

import slieb.kute.resources.Resource;
import slieb.kute.resources.ResourceFilter;

public class AllFilter implements ResourceFilter {
    private final ResourceFilter[] filters;

    public AllFilter(ResourceFilter... filters) {
        this.filters = filters;
    }

    @Override
    public Boolean accepts(Resource resource) {
        for(ResourceFilter filter : filters) {
            if(!filter.accepts(resource)) {
                return false;
            }
        }
        return true;
    }
}
