package slieb.kute.api;

import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.sun.org.apache.xerces.internal.impl.dv.util.HexBin.encode;
import static org.junit.Assert.assertEquals;
import static slieb.kute.api.TestUtils.*;

public class ResourceChecksumableTest {

    @Test
    public void testMD5ChecksumResult() throws IOException, NoSuchAlgorithmException {
        assertEquals(EXPECTED_MD5_CHECKSUM, encode(checksumable(EXPECTED_CONTENT).checksum("MD5")));
    }

    @Test
    public void testSHAChecksumResult() throws IOException, NoSuchAlgorithmException {
        assertEquals(EXPECTED_SHA_CHECKSUM, encode(checksumable(EXPECTED_CONTENT).checksum("SHA")));
    }
}
