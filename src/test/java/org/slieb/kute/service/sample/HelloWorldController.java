package org.slieb.kute.service.sample;

import org.slieb.kute.service.annotations.KuteAction;
import org.slieb.kute.service.annotations.KuteBefore;
import org.slieb.kute.service.annotations.KuteController;
import spark.Request;
import spark.Response;

import java.util.Optional;

@KuteController
public class HelloWorldController {

    @KuteBefore
    public void before(Request request,
                       Response response) {
        response.header("customHeader", "sneaky");
    }

    @KuteAction("/greeting")
    public String getGreeting(Request request,
                              Response response,
                              Optional previous) {
        return "Hello World";
    }

}
