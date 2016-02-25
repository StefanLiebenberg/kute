package org.slieb.kute.providers;

import org.slieb.kute.Kute;
import org.slieb.kute.api.Resource;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class RenamedNamespaceProvider implements Resource.Provider, Serializable {

    private final Resource.Provider resourceProvider;

    private final String originalRoot;

    private final String renamedRoot;

    public RenamedNamespaceProvider(Resource.Provider resourceProvider,
                                    String originalRoot,
                                    String renamedRoot) {
        this.resourceProvider = resourceProvider;
        this.originalRoot = originalRoot;
        this.renamedRoot = renamedRoot;
    }

    private static String getRenamedPath(String path,
                                         String origPath,
                                         String newPath) {
        if (path != null) {
            return path.replaceFirst(origPath, newPath);
        } else {
            return null;
        }
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        return resourceProvider.getResourceByName(getRenamedPath(path, renamedRoot, originalRoot))
                               .map(resource -> Kute.renameResource(path, resource));
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return Kute.distinctPath(resourceProvider.stream().map(this::renameResource));
    }

    private Resource.Readable renameResource(Resource.Readable resource) {
        return Kute.renameResource(getRenamedPath(resource.getPath(), originalRoot, renamedRoot), resource);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof RenamedNamespaceProvider)) { return false; }
        RenamedNamespaceProvider readables = (RenamedNamespaceProvider) o;
        return Objects.equals(resourceProvider, readables.resourceProvider) &&
                Objects.equals(originalRoot, readables.originalRoot) &&
                Objects.equals(renamedRoot, readables.renamedRoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceProvider, originalRoot, renamedRoot);
    }

    @Override
    public String toString() {
        return "RenamedNamespaceProvider{" +
                "resourceProvider=" + resourceProvider +
                ", originalRoot='" + originalRoot + '\'' +
                ", renamedRoot='" + renamedRoot + '\'' +
                '}';
    }
}
