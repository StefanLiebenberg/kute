package org.slieb.kute.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.sun.org.apache.xerces.internal.impl.dv.util.HexBin.encode;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public final class ResourceProviderTest {

    Resource.Readable resourceA, resourceB, resourceC;

    @Before
    public void setup() {
        resourceA = TestUtils.readableResource("/path/a", "A");
        resourceB = TestUtils.readableResource("/path/b", "B");
        resourceC = TestUtils.readableResource("/path/c", "C");
    }

    @Test
    public void testCollectionResources() throws Exception {
        Resource.Provider provider = () -> Stream.of(resourceA, resourceB, resourceC);
        assertEquals(Optional.of(resourceA), provider.getResourceByName("/path/a"));
        assertEquals(Optional.of(resourceB), provider.getResourceByName("/path/b"));
        assertEquals(Optional.of(resourceC), provider.getResourceByName("/path/c"));
        assertFalse(provider.getResourceByName("/path/d").isPresent());
        Iterator<Resource.Readable> iterator = provider.iterator();
        Set<Resource> resources = new HashSet<>();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            assertFalse(resources.contains(resource));
            resources.add(resource);
        }
        assertEquals(3, resources.size());
        assertEquals(Stream.of(resourceA, resourceB, resourceC).collect(toSet()),
                     provider.stream().collect(toSet()));
    }

    @Test
    public void testProviderChecksumMD5() throws IOException, NoSuchAlgorithmException {
        Resource.Provider provider = () -> Stream.of(TestUtils.readableResource(TestUtils.EXPECTED_PATH, TestUtils.EXPECTED_CONTENT));
        Assert.assertEquals(TestUtils.EXPECTED_MD5_CHECKSUM, encode(provider.checksum("MD5")));
    }

    @Test
    public void testProviderChecksumSHA() throws IOException, NoSuchAlgorithmException {
        Resource.Provider provider = () -> Stream.of(TestUtils.readableResource(TestUtils.EXPECTED_PATH, TestUtils.EXPECTED_CONTENT));
        Assert.assertEquals(TestUtils.EXPECTED_SHA_CHECKSUM, encode(provider.checksum("SHA")));
    }
}

