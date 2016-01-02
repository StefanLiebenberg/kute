package org.slieb.kute.service.resources;


import slieb.kute.resources.ContentResource;

public abstract class AbstractHTMLResource implements ContentResource, HasContentType {

    private final String path;

    public AbstractHTMLResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

}
