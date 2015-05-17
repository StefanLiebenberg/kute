package org.slieb.kute.service;


import com.google.common.collect.ImmutableList;
import org.slieb.kute.service.providers.IndexProvider;
import slieb.kute.Kute;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.GroupResourceProvider;
import spark.Spark;

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

    public static void main(String[] args) throws InterruptedException {
        ResourceProvider<? extends Resource.Readable> sources = Kute.getDefaultProvider();
        ResourceProvider<? extends Resource.Readable> index = new IndexProvider(sources);
        ResourceProvider<? extends Resource.Readable> provider = new GroupResourceProvider<>(ImmutableList.of(sources, index));
        Service service = new Service(provider, 5000);
        service.start();
    }

}
