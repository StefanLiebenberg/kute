package org.slieb.kute.service;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ServiceExceptionHandler implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request request, Response response) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(boas);
        e.printStackTrace(printStream);
        response.body(boas.toString());
        System.out.println(boas.toString());
    }
}
