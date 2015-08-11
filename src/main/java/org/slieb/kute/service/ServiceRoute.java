package org.slieb.kute.service;

import org.apache.commons.io.IOUtils;
import org.slieb.kute.service.resources.ServiceResource;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static slieb.kute.resources.Resources.readResource;


public class ServiceRoute implements Route {

    private final ResourceProvider<? extends Resource.Readable> provider;

    public ServiceRoute(ResourceProvider<? extends Resource.Readable> provider) {
        this.provider = provider;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Resource.Readable resource = provider.getResourceByName(request.pathInfo());
        if (resource != null) {
            response.type(getContentType(resource));
            if (isInputStreamingResource(resource)) {
                try (InputStream inputStream = ((Resource.InputStreaming) resource).getInputStream();
                     OutputStream outputStream = response.raw().getOutputStream()) {
                    IOUtils.copy(inputStream, outputStream);
                }
                return "";
            } else {
                return readResource(resource);
            }
        }
        throw new FileNotFoundException(request.pathInfo());
    }

    public boolean isInputStreamingResource(Resource resource) {
        if (resource instanceof Resource.Proxy) {
            return isInputStreamingResource(((Resource.Proxy) resource).getResource());
        }
        return resource instanceof Resource.InputStreaming;
    }


    public String getContentType(Resource resource) throws IOException {
        if (resource instanceof ServiceResource) {
            return ((ServiceResource) resource).getContentType();
        }
        return Files.probeContentType(Paths.get(resource.getPath()));
    }
}
