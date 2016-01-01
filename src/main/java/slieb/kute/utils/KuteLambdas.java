package slieb.kute.utils;


import com.google.common.collect.Maps;
import slieb.kute.api.Resource;
import slieb.kute.utils.interfaces.*;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

public class KuteLambdas {

    public static <T extends Resource> ResourceConsumer<T> unsafeConsumer(final ResourceConsumerWithIO<T> resourceConsumerWithIO) {
        return (object) -> {
            try {
                resourceConsumerWithIO.acceptWithIO(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <A extends Resource, B> ResourceFunction<A, B> unsafeMap(final ResourceFunctionWithIO<A, B> mapFunction) {
        return (a) -> {
            try {
                return mapFunction.applyWithIO(a);
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        };
    }

    public static <T> Supplier<T> unsafeSupply(SupplierWithIO<T> resourceSupplier) {
        return () -> {
            try {
                return resourceSupplier.getWithIO();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
//
//    public static <T extends Resource> ResourceSupplier<T> unsafeSupplyResource(ResourceSupplierWithIO<T> resourceSupplier) {
//        return () -> {
//            try {
//                return resourceSupplier.getWithIO();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };
//    }
//
//    public static ResourcePredicate unsafeTest(PredicateWithIO predicateWithIO) {
//        return (object) -> {
//            try {
//                return predicateWithIO.testWithIO(object);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };
//    }


    /**
     * @param <A> A implementation of resource.
     * @return A nonNull predicate.
     */
    public static <A extends Resource> Predicate<A> nonNull() {
        return p -> p != null;
    }

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> Predicate<A> all(Predicate<A>... predicates) {
        return stream(predicates).reduce(Predicate::and).orElse((a) -> true);
    }

//    /**
//     * @param <A>        A implementation of resource.
//     * @param predicates Predicates to string together.
//     * @return a single predicate.
//     */
//    @SafeVarargs
//    public static <A extends Resource> Predicate<A> any(final Predicate<A>... predicates) {
//        return stream(predicates).reduce(Predicate::or).orElse((a) -> true);
//    }
//
//    /**
//     * @param <A>        A implementation of resource.
//     * @param predicates Predicates to string together.
//     * @return a single predicate.
//     */
//    @SafeVarargs
//    public static <A extends Resource> Predicate<A> none(Predicate<A>... predicates) {
//        return all(predicates).negate();
//    }

    /**
     * @param extensions A variable list of extension strings.
     * @return True resource path ends with any of the extension strings.
     */
    public static ResourcePredicate<Resource> extensionFilter(String... extensions) {
        return (resource) -> KutePredicates.resourceHasExtension(resource, extensions);
    }

    /**
     * @param pattern A Pattern to match against the resource path
     * @return true if the resource path matches the specified pattern.
     */
    public static ResourcePredicate<Resource> patternFilter(Pattern pattern) {
        return (r) -> pattern.matcher(r.getPath()).matches();
    }

    /**
     * @param pattern A string Pattern to match against the resource path
     * @return true if the pattern matches the resource path.
     */
    public static ResourcePredicate<Resource> patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }

    /**
     * A predicate to filter out already seen resources by function.
     *
     * @param function The function to determine the resource value.
     * @param <R>      The Resource implementation.
     * @param <X>      The Value type.
     * @return A stateful predicate.
     */
    public static <R extends Resource, X> Predicate<R> distinctFilter(Function<R, X> function) {
        final Map<X, Boolean> seen = Maps.newConcurrentMap();
        return resource -> seen.putIfAbsent(function.apply(resource), Boolean.TRUE) == null;
    }
}
