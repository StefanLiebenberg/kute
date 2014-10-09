package slieb.kute.resources.filters;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceFilter;

public class ExtensionFilter implements ResourceFilter {

    private final String[] extensions;

    public ExtensionFilter(String... extensions) {
        this.extensions = extensions;
    }

    @Override
    public Boolean accepts(Resource resource) {
        String ns = resource.getPath();
        for (String ext : extensions) {
            if (ns.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
