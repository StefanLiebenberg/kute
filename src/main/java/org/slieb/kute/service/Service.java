package org.slieb.kute.service;


import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.slieb.kute.service.providers.IndexProvider;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.GroupResourceProvider;
import spark.Spark;
import spark.SparkBase;

import java.util.List;

public class Service {

    private final ResourceProvider<? extends Resource.Readable> provider;

    private final Integer port;

    private boolean started = false;

    /**
     * @param provider A provider where resources may be found.
     * @param port The port number to run service on.
     */
    public Service(ResourceProvider<? extends Resource.Readable> provider, Integer port) {
        this.provider = provider;
        this.port = port;
    }

    public void start() throws InterruptedException {
        Preconditions.checkState(!started, "Server already started");
        Spark.port(port);
        Spark.post("/shutdown", (request, response) -> {
            response.raw().getOutputStream().print("done");
            response.raw().getOutputStream().close();
            response.raw().flushBuffer();
            stop();
            return "";
        });
        Spark.get("/shutdown", (request, response) -> "<form method=POST action=/shutdown><input type=\"submit\" value=\"Shutdown\"></form>");
        Spark.get("/*", new ServiceRoute(provider));
        Spark.exception(Exception.class, new ServiceExceptionHandler());
        SparkBase.awaitInitialization();
        started = true;
    }

    public void stop() {
        Preconditions.checkState(started, "server not started yet");
        Spark.stop();
        started = false;
    }

    public boolean stopped() {
        return !started;
    }

    public boolean started() {
        return started;
    }


    public static void main(String[] args) throws Exception {
//        ResourceProvider<? extends Resource.Readable> resources = Kute.providerOf(Kute.getDefaultProvider().stream().collect(Collectors.toList()));
        ResourceProvider<? extends Resource.Readable> resources = Kute.getDefaultProvider();
        ResourceProvider<? extends Resource.Readable> indexer = new IndexProvider(resources);
        List<ResourceProvider<? extends Resource.Readable>> list = ImmutableList.of(resources, indexer);
        Service service = new Service(new GroupResourceProvider<>(list), 9666);
        service.start();
    }

}


