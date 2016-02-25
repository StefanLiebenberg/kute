package org.slieb.kute.api;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResourceReadableTest {

    Resource.Readable readable;

    @Before
    public void setUp() throws Exception {
        readable = TestUtils.readableResource(TestUtils.EXPECTED_PATH, TestUtils.EXPECTED_CONTENT);
    }

    @Test
    public void testResourceReadableDefaultGetReader() throws IOException {
        try (Reader reader = readable.getReader()) {
            assertNotNull(reader);
            Assert.assertEquals(TestUtils.EXPECTED_PATH, readable.getPath());
            Assert.assertEquals(TestUtils.EXPECTED_CONTENT, IOUtils.toString(reader));
        }
    }

    @Test
    public void testResourceReadableUseReader() throws IOException {
        readable.useReader(reader -> {
            assertNotNull(reader);
            Assert.assertEquals(TestUtils.EXPECTED_PATH, readable.getPath());
            Assert.assertEquals(TestUtils.EXPECTED_CONTENT, IOUtils.toString(reader));
        });
    }

    @Test(expected = IOException.class)
    public void testResourceReadableUseReaderThrowsIO() throws IOException {
        readable.useReader(reader -> {
            throw new IOException();
        });
    }

    @Test
    public void testResourceReadableUseInputStream() throws IOException {
        readable.useInputStream(inputStream -> {
            assertNotNull(inputStream);
            Assert.assertEquals(TestUtils.EXPECTED_PATH, readable.getPath());
            Assert.assertEquals(TestUtils.EXPECTED_CONTENT, IOUtils.toString(inputStream));
        });
    }

    @Test(expected = IOException.class)
    public void testResourceReadableUseInputStreamThrowsIO() throws IOException {
        readable.useInputStream(inputStream -> {
            throw new IOException();
        });
    }

    @Test
    public void testResourceReadableUpdateDigest() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        readable.updateDigest(digest);
        String result = HexBin.encode(digest.digest());
        Assert.assertEquals(TestUtils.EXPECTED_MD5_CHECKSUM, result);
    }
}