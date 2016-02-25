package org.slieb.kute.service.internal;

import spark.Request;
import spark.Response;

import java.util.Optional;


public class State {

    private final Request request;
    private final Response response;
    private Object result;

    public State(Request request,
                 Response response) {
        this.request = request;
        this.response = response;
    }

    public Optional<Object> getResult() {
        return Optional.ofNullable(response);
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

}
