package slieb.kute.utils;


import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class KutePredicates {

    /**
     * @param a         A resource to compare
     * @param resourceB A resource to compare
     * @return True if both resources have the same path
     */
    public static boolean resourcePathEquals(Resource a, Resource resourceB) {
        return Objects.equals(a.getPath(), resourceB.getPath());
    }

    /**
     * @param resourceA Readable resource
     * @param resourceB Readable resource
     * @return True if both resources have the same content
     * @throws IOException throws an ioException
     */
    public static boolean resourceContentEquals(Resource.Readable resourceA, Resource.Readable resourceB) throws IOException {
        try (InputStream inputStreamA = resourceA.getInputStream();
             InputStream inputStreamB = resourceB.getInputStream()) {
            return IOUtils.contentEquals(inputStreamA, inputStreamB);
        }
    }

    /**
     * @param resourceA Readable resource
     * @param resourceB Readable resource
     * @return True if both resources have the same path and content
     * @throws IOException throws an ioException
     */
    public static boolean resourceEquals(Resource.Readable resourceA, Resource.Readable resourceB) throws IOException {
        return resourcePathEquals(resourceA, resourceB) && resourceContentEquals(resourceA, resourceB);
    }


    /**
     * @param resource   A resource
     * @param extensions An array of extensions to check
     * @return Returns true if resource has given extension.
     */
    public static boolean resourceHasExtension(Resource resource, String... extensions) {
        return Arrays.stream(extensions).anyMatch(resource.getPath()::endsWith);
    }
}
