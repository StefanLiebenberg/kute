package slieb.kute.resources;


import slieb.kute.api.Resource;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class ResourceFilters {


    /**
     * @param extensions A variable list of extension strings.
     * @return True resource path ends with any of the extension strings.
     */
    public static Predicate<Resource> extensionFilter(String... extensions) {
        return (r) -> asList(extensions).stream().anyMatch(r.getPath()::endsWith);
    }

    /**
     * @param pattern A Pattern to match against the resource path
     * @return true if the resource path matches the specified pattern.
     */
    public static Predicate<Resource> patternFilter(Pattern pattern) {
        return (r) -> pattern.matcher(r.getPath()).matches();
    }

    /**
     * @param pattern A string Pattern to match against the resource path
     * @return true if the pattern matches the resource path.
     */
    public static Predicate<Resource> patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }


}
