package org.slieb.kute.service;

import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.slieb.sparks.Sparks.getContentType;


public class ResourcesRoute implements Route {

    private final Resource.Provider resourceProvider;

    public ResourcesRoute(Resource.Provider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    @Override
    public Object handle(final Request request,
                         final Response response) throws Exception {
        try {
            return resourceProvider.getResourceByName(request.pathInfo())
                    .map(readable -> handleReadable(request, response, readable))
                    .orElse(null);
        } catch (InvisibleException e) {
            throw e.getIoException();
        }
    }

    private String handleReadable(Request request,
                                  Response response,
                                  Resource.Readable readable) {
        try {
            response.type(getContentType(request));
            try (final InputStream inputStream = readable.getInputStream();
                 final OutputStream outputStream = response.raw().getOutputStream()) {
                IOUtils.copy(inputStream, outputStream);
            }
            return "";
        } catch (IOException e) {
            throw new InvisibleException(e);
        }
    }
}

class InvisibleException extends RuntimeException {
    private final IOException ioException;

    public InvisibleException(IOException cause) {
        super(cause);
        this.ioException = cause;
    }

    public IOException getIoException() {
        return ioException;
    }
}