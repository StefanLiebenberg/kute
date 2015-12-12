package org.slieb.kute.service.sample;

import org.slieb.kute.service.KuteService;
import org.slieb.kute.service.annotations.KuteAction;
import org.slieb.kute.service.annotations.KuteBefore;
import org.slieb.kute.service.annotations.KuteController;
import slieb.kute.Kute;
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

    public static void main(String[] arguments) throws InterruptedException {
        KuteService service = new KuteService();
        service.addResourceProvider(Kute.getDefaultProvider());
        service.addController(new UserController());
        service.addController(new HelloWorldController());
        service.start();
    }

}
