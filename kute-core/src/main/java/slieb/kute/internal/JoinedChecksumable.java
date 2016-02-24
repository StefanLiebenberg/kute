package slieb.kute.internal;

import slieb.kute.api.Resource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


public class JoinedChecksumable implements Resource.Checksumable {

    private final List<Resource.Checksumable> collection;

    public JoinedChecksumable(List<Resource.Checksumable> collection) {
        this.collection = collection;
    }

    public JoinedChecksumable(Resource.Checksumable[] array) {
        this.collection = Arrays.stream(array).collect(toList());
    }

    @Override
    public void updateDigest(MessageDigest digest) throws IOException {
        for (Resource.Checksumable checksumable : collection) {
            checksumable.updateDigest(digest);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinedChecksumable)) return false;
        JoinedChecksumable that = (JoinedChecksumable) o;
        return Objects.equals(collection, that.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection);
    }

    @Override
    public String toString() {
        return "JoinedChecksumable{" +
                "collection=" + collection +
                '}';
    }
}
