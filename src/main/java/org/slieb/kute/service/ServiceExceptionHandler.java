package org.slieb.kute.service;

import org.slieb.kute.service.resources.ErrorResource;
import slieb.kute.Kute;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import java.io.IOException;


public class ServiceExceptionHandler implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request request, Response response) {
        try {
            response.body(Kute.readResource(new ErrorResource(request.pathInfo(), e)));
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}
