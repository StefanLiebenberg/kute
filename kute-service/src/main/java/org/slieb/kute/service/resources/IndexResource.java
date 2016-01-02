package org.slieb.kute.service.resources;


import slieb.kute.KuteIO;
import slieb.kute.api.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;


public class IndexResource extends AbstractHTMLResource {

    private final Resource.Provider provider;

    private final Resource.Readable indexResource;

    public IndexResource(String path,
                         Resource.Provider provider,
                         Resource.Readable indexResource) {
        super(path);
        this.provider = provider;
        this.indexResource = indexResource;
    }

    public boolean hasIndexResource() {
        return indexResource != null;
    }

    @Override
    public String getContent() throws IOException {

        if (indexResource != null) {
            return KuteIO.readResource(indexResource);
        }

        Path pathObj = Paths.get(getPath());
        StringBuilder result = new StringBuilder();
        result.append("<h1>Index Of ").append(getPath()).append("</h1>");
        result.append("<ul>");
        Path parentPath = pathObj.getParent();
        if (parentPath != null) {
            result.append("<li>");
            result.append(appendAnchor(parentPath.toString(), "Up to Parent"));
            result.append("</li>");
        }
        getChildrenStream(pathObj).map(
                p -> "<li>" + appendAnchor(p.toString(), p.getFileName().toString()) + "</li>").forEachOrdered(
                result::append);
        result.append("</ul>");
        return result.toString();
    }

    private Stream<Path> getNodeTreeStream(Resource resource) {
        Stream.Builder<Path> pathBuilder = Stream.builder();
        for (Path current = Paths.get(resource.getPath()); current != null; current = current.getParent()) {
            pathBuilder.accept(current);
        }
        return pathBuilder.build();
    }

    protected Stream<Path> getChildrenStream(Path path) {
        return provider.stream().parallel().flatMap(this::getNodeTreeStream).filter(
                ((Function<Path, Boolean>) path::equals).compose(Path::getParent)::apply).distinct().sorted();
    }

    private String appendAnchor(String href,
                                String name) {
        return "<a href='" + href + "'>" + name + "</a>";
    }


}

