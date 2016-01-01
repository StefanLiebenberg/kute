package slieb.kute.utils.interfaces;

import slieb.kute.api.Resource;

@FunctionalInterface
public interface ResourceSupplierWithIO<T extends Resource> extends SupplierWithIO<T> {
}

