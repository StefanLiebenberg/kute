package slieb.kute.utils;


import slieb.kute.api.Resource;
import slieb.kute.utils.internal.JoinedChecksumable;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class KuteDigest {

    public static Resource.Checksumable join(Resource.Checksumable... checksumables) {
        return new JoinedChecksumable(checksumables);
    }

    public static Resource.Checksumable join(List<Resource.Checksumable> checksumables) {
        return new JoinedChecksumable(checksumables);
    }

    public static byte[] md5(Resource.Checksumable resourceProvider) {
        try {
            return resourceProvider.checksum("MD5");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to calculate md5", e);
        }
    }

    public static byte[] sha1(Resource.Checksumable resourceProvider) {
        try {
            return resourceProvider.checksum("SHA1");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to calculate md5", e);
        }
    }
}
