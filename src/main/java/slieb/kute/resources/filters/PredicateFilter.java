package slieb.kute.resources.filters;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

import java.util.function.Predicate;

public class PredicateFilter implements ResourceFilter {

    private final Predicate<Resource> predicate;

    public PredicateFilter(Predicate<Resource> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Boolean accepts(Resource resource) {
        return predicate.test(resource);
    }
}
