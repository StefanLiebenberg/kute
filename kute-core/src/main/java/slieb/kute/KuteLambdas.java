package slieb.kute;


import org.slieb.throwables.FunctionWithThrowable;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.util.regex.Pattern;

@Deprecated
@SuppressWarnings("unused")
public class KuteLambdas {

    @Deprecated
    public static Resource.Predicate nonNull() {
        return KutePredicates.nonNull();
    }

    @Deprecated
    public static Resource.Predicate all(Resource.Predicate... predicates) {
        return KutePredicates.all(predicates);
    }
    
    /**
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @Deprecated
    public static Resource.Predicate none(Resource.Predicate... predicates) {
        return KutePredicates.none(predicates);
    }

    @Deprecated
    public static Resource.Predicate any(final Resource.Predicate... predicates) {
        return KutePredicates.any(predicates);
    }

    @Deprecated
    public static Resource.Predicate extensionFilter(String... extensions) {
        return KutePredicates.extensionFilter(extensions);
    }

    @Deprecated
    public static Resource.Predicate patternFilter(Pattern pattern) {
        return KutePredicates.patternFilter(pattern);
    }

    @Deprecated
    public static Resource.Predicate patternFilter(String pattern) {
        return KutePredicates.patternFilter(pattern);
    }

    @Deprecated
    public static <R extends Resource, X> Resource.Predicate distinctFilter(FunctionWithThrowable<Resource, X, IOException> function) {
        return KutePredicates.distinctFilter(function);
    }
}
