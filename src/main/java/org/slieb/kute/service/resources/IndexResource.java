package org.slieb.kute.service.resources;

import org.slieb.kute.service.providers.IndexProvider;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;


public class IndexResource implements Resource.Readable {

    private final String path;

    private final ResourceProvider<? extends Resource.Readable> provider;

    public IndexResource(String path, ResourceProvider<? extends Readable> provider) {
        this.path = path;
        this.provider = provider;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Reader getReader() throws IOException {
        StringBuilder result = new StringBuilder();
        Path pathObj = Paths.get(path);

        result.append("<h1>Index Of ").append(path).append("</h1>");

        result.append("<ul>");
        Path parentPath = pathObj.getParent();
        if (parentPath != null) {
            result.append("<li>");
            appendAnchor(result, parentPath.toString(), "Up to Parent");
            result.append("</li>");
        }

        provider
                .stream()
                .map(Resource::getPath)
                .map(Paths::get)
                .flatMap(IndexProvider::parentsStream)
                .distinct()
                .filter(p -> pathObj.equals(p.getParent()))
                .forEach(p -> {
                    result.append("<li>");
                    appendAnchor(result, p.toString(), p.getFileName().toString());
                    result.append("</li>");
                });
        result.append("</ul>");
        return new StringReader(result.toString());
    }

    private void appendAnchor(StringBuilder str, String href, String name) {
        str.append("<a href='").append(href).append("'>").append(name).append("</a>");
    }
}
