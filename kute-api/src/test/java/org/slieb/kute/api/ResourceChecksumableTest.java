package org.slieb.kute.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.sun.org.apache.xerces.internal.impl.dv.util.HexBin.encode;

public class ResourceChecksumableTest {

    @Test
    public void testMD5ChecksumResult() throws IOException, NoSuchAlgorithmException {
        Assert.assertEquals(TestUtils.EXPECTED_MD5_CHECKSUM, encode(TestUtils.checksumable(TestUtils.EXPECTED_CONTENT).checksum("MD5")));
    }

    @Test
    public void testSHAChecksumResult() throws IOException, NoSuchAlgorithmException {
        Assert.assertEquals(TestUtils.EXPECTED_SHA_CHECKSUM, encode(TestUtils.checksumable(TestUtils.EXPECTED_CONTENT).checksum("SHA")));
    }
}
