package slieb.kute.utils;


import com.google.common.collect.Maps;
import slieb.kute.api.Resource;
import slieb.kute.utils.ConsumerWithIO;
import slieb.kute.utils.FunctionWithIO;
import slieb.kute.utils.PredicateWithIO;
import slieb.kute.utils.SupplierWithIO;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

public class KuteLambdas {

    public static <T> Consumer<T> safelyConsume(final ConsumerWithIO<T> consumerWithIO) {
        return (object) -> {
            try {
                consumerWithIO.acceptWithIO(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <A, B> Function<A, B> safelyMapWithIO(final FunctionWithIO<A, B> mapFunction) {
        return (a) -> {
            try {
                return mapFunction.applyWithIO(a);
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        };
    }

    public static <T> Supplier<T> safelySupply(SupplierWithIO<T> supplierWithIO) {
        return () -> {
            try {
                return supplierWithIO.getWithIO();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T> Predicate<T> safelyTest(PredicateWithIO<T> predicateWithIO) {
        return (object) -> {
            try {
                return predicateWithIO.testWithIO(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }


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

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> Predicate<A> any(final Predicate<A>... predicates) {
        return stream(predicates).reduce(Predicate::or).orElse((a) -> true);
    }

    /**
     * @param <A>        A implementation of resource.
     * @param predicates Predicates to string together.
     * @return a single predicate.
     */
    @SafeVarargs
    public static <A extends Resource> Predicate<A> none(Predicate<A>... predicates) {
        return all(predicates).negate();
    }

    /**
     * @param extensions A variable list of extension strings.
     * @return True resource path ends with any of the extension strings.
     */
    public static Predicate<Resource> extensionFilter(String... extensions) {
        return (r) -> stream(extensions).anyMatch(r.getPath()::endsWith);
    }

    /**
     * @param <A>     A implementation of resource.
     * @param pattern A Pattern to match against the resource path
     * @return true if the resource path matches the specified pattern.
     */
    public static <A extends Resource> Predicate<A> patternFilter(Pattern pattern) {
        return (r) -> pattern.matcher(r.getPath()).matches();
    }

    /**
     * @param <A>     A implementation of resource.
     * @param pattern A string Pattern to match against the resource path
     * @return true if the pattern matches the resource path.
     */
    public static <A extends Resource> Predicate<A> patternFilter(String pattern) {
        return patternFilter(Pattern.compile(pattern));
    }

    /**
     * A predicate to filter out already seen resources by function.
     *
     * @param function The function to determine the resource value.
     * @param <R>      The Resource implementation.
     * @param <X>      The Value type.
     * @return A statefull predicate.
     */
    public static <R extends Resource, X> Predicate<R> distinctFilter(Function<R, X> function) {
        final Map<X, Boolean> seen = Maps.newConcurrentMap();
        return resource -> seen.putIfAbsent(function.apply(resource), Boolean.TRUE) == null;
    }
}
