package slieb.kute.utils;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class KuteDigest {

    public static Resource.Checksumable join(Resource.Checksumable... checksumables) {
        return digest -> {
            for (Resource.Checksumable checksumable : checksumables) {
                checksumable.updateDigest(digest);
            }
        };
    }

    public static Resource.Checksumable join(Iterable<Resource.Checksumable> checksumables) {
        return digest -> {
            for (Resource.Checksumable checksumable : checksumables) {
                checksumable.updateDigest(digest);
            }
        };
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