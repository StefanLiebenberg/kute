package org.slieb.kute.service;


import com.google.common.collect.ImmutableList;
import org.slieb.kute.service.providers.IndexProvider;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.GroupResourceProvider;
import spark.Spark;

import java.util.stream.Stream;

public class Service {

    private final ResourceProvider<? extends Resource.Readable> provider;

    private final Integer port;

    public Service(ResourceProvider<? extends Resource.Readable> provider, Integer port) {
        this.provider = provider;
        this.port = port;
    }

    public void start() throws InterruptedException {
        Spark.port(port);
        Spark.get("/*", new ServiceRoute(provider));
        Spark.exception(RuntimeException.class, new ServiceExceptionHandler());
        Thread.sleep(1000);
    }

    public void stop() {
        Spark.stop();
    }


}
