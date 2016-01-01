package slieb.kute.utils.interfaces;

import slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.function.Predicate;


@FunctionalInterface
public interface ResourcePredicate<T extends Resource> extends Predicate<T>, Serializable {
}
