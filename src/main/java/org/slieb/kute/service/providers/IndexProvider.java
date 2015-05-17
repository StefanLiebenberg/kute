package org.slieb.kute.service.providers;


import org.slieb.kute.service.resources.IndexResource;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class IndexProvider implements ResourceProvider<Resource.Readable> {

    public final ResourceProvider<? extends Resource.Readable> provider;

    public IndexProvider(ResourceProvider<? extends Resource.Readable> provider) {
        this.provider = provider;
    }

    @Override
    public Resource.Readable getResourceByName(String path) {

        Resource resource = provider.getResourceByName(path);
        if (resource != null && resource instanceof Resource.Readable) {
            return (Resource.Readable) resource;
        }

        Path htmlPath = Paths.get(path, "index.html");
        Resource htmlResource = provider.getResourceByName(htmlPath.toString());
        if (htmlResource != null && htmlResource instanceof Resource.Readable) {
            return (Resource.Readable) htmlResource;
        }

        Path pathObj = Paths.get(path).normalize();
        if (directoriesStream().anyMatch(pathObj::equals)) {
            return new IndexResource(path, provider);
        }
        return null;
    }

    protected Stream<Path> directoriesStream() {
        return provider.stream()
                .map(Resource::getPath)
                .map(Paths::get)
                .map(Path::getParent)
                .flatMap(IndexProvider::parentsStream)
                .map(Path::normalize)
                .distinct();
    }

    public static Stream<Path> parentsStream(Path path) {
        Set<Path> paths = new HashSet<>();
        Path current = path;
        while (current != null) {
            paths.add(current);
            current = current.getParent();
        }
        return Stream.of(paths.stream().toArray(Path[]::new));
    }


    @Override
    public Stream<Resource.Readable> stream() {
        return directoriesStream()
                .map(p -> new IndexResource(p.toString(), this));
    }
}
