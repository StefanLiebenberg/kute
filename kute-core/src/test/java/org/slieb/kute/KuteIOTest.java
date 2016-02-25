package org.slieb.kute;

import org.junit.Test;
import org.slieb.kute.api.Resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.slieb.kute.Kute.*;
import static org.slieb.kute.KutePredicates.*;


public class KuteIOTest {

    @Test
    public void testResourcePathEquals() throws Exception {
        assertTrue(resourcePathEquals(stringResource("/path", "a"), stringResource("/path", "b")));
        assertFalse(resourcePathEquals(stringResource("/path", "a"), stringResource("/path_1", "b")));
    }


    @Test
    public void testResourceContentEquals() throws Exception {
        assertTrue(resourceContentEquals(stringResource("/path", "content a"), stringResource("/path", "content a")));
        assertFalse(resourceContentEquals(stringResource("/path", "content a"), stringResource("/path", "content A")));
    }

    @Test
    public void testEquals() throws Exception {
        Resource.Readable resource = stringResource("/path", "content");
        Resource.Readable alternative = stringResource("/path", "alternative");
        Resource.Readable copiedResource = immutableMemoryResource(resource);
        Resource.Readable renamed = renameResource("/other", resource);

        assertTrue(resourceEquals(resource, copiedResource));
        assertFalse(resourceEquals(resource, alternative));
        assertFalse(resourceEquals(renamed, resource));
        assertFalse(resourceEquals(renamed, copiedResource));
        assertFalse(resourceEquals(renamed, alternative));
    }


}