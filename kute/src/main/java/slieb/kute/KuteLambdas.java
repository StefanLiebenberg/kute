package slieb.kute;


import org.slieb.throwables.FunctionWithThrowable;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourcePredicate;

import java.io.IOException;
import java.util.regex.Pattern;

@Deprecated
public class KuteLambdas {

    @Deprecated
    public static <A extends Resource> ResourcePredicate<A> nonNull() {
        return KutePredicates.nonNull();
    }

    @Deprecated
    @SafeVarargs
    public static <A extends Resource> ResourcePredicate<A> all(ResourcePredicate<A>... predicates) {
        return KutePredicates.all(predicates);
    }

    @Deprecated
    @SafeVarargs
    public static <A extends Resource> ResourcePredicate<A> any(final ResourcePredicate<A>... predicates) {
        return KutePredicates.any(predicates);
    }

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> ResourcePredicate<A> none(ResourcePredicate<A>... predicates) {
        return KutePredicates.none(predicates);
    }

    @Deprecated
    public static ResourcePredicate<Resource> extensionFilter(String... extensions) {
        return KutePredicates.extensionFilter(extensions);
    }

    @Deprecated
    public static ResourcePredicate<Resource> patternFilter(Pattern pattern) {
        return KutePredicates.patternFilter(pattern);
    }

    @Deprecated
    public static ResourcePredicate<Resource> patternFilter(String pattern) {
        return KutePredicates.patternFilter(pattern);
    }

    @Deprecated
    public static <R extends Resource, X> ResourcePredicate<R> distinctFilter(FunctionWithThrowable<R, X, IOException> function) {
        return KutePredicates.distinctFilter(function);
    }
}
