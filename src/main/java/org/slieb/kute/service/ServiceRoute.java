package org.slieb.kute.service;

import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import spark.Request;
import spark.Response;
import spark.Route;

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
        long startTime = System.nanoTime();
        try {
            response.type(getContentType(request, response));
            Resource.Readable resource = getReadable(request);
            if (isInputStreamingResource(resource)) {
                return handleInputStreaming((Resource.InputStreaming) resource, request, response);
            }
            return readResource(getReadable(request));
        } finally {
            System.out.println(request.pathInfo() + ": " + (System.nanoTime() - startTime) / 1000000);
        }
    }

    public boolean isInputStreamingResource(Resource resource) {
        return resource instanceof Resource.Proxy ?
                isInputStreamingResource(((Resource.Proxy) resource).getResource()) :
                resource instanceof Resource.InputStreaming;
    }

    
    public Object handleInputStreaming(Resource.InputStreaming inputStreaming, Request request, Response response) throws IOException {
        try (InputStream inputStream = inputStreaming.getInputStream();
             OutputStream outputStream = response.raw().getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
        return "";
    }

    public Resource.Readable getReadable(Request request) {
        Resource.Readable readable = provider.getResourceByName(request.pathInfo());
        if (readable != null) {
            return readable;
        }
        throw new RuntimeException("Resource not Found");
    }

    public String getContentType(Request request, Response response) throws IOException {
        String path = request.pathInfo();
        if (path.endsWith("/")) {
            return "text/html";
        }
        return Files.probeContentType(Paths.get(path));
    }
}
