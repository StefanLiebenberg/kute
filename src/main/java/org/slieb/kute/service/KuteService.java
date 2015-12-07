package org.slieb.kute.service;


import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import spark.SparkInstance;

import static org.slieb.sparks.Sparks.addServerStopEndpoints;

public class KuteService {

    public final ImmutableCollection<ResourceProvider<? extends Resource.Readable>> resourceProviders;


    private boolean started = false;

    private SparkInstance sparkInstance;

    private KuteFactory factory;


    public KuteService(SparkInstance sparkInstance,
                       ImmutableCollection<ResourceProvider<? extends Resource.Readable>> resourceProviders) {
        this.sparkInstance = sparkInstance;
        this.resourceProviders = resourceProviders;
        this.factory = new KuteFactory();
    }

    public void addController(Object controller) {
        KuteControllerNode node = factory.getControllerNode(controller);
        node.accept(sparkInstance);
    }

    public void start() throws InterruptedException {
        Preconditions.checkState(!started, "Server already started");
        sparkInstance.get("/*", ((request, response) -> null));
        sparkInstance.exception(Exception.class, new ServiceExceptionHandler());
        addServerStopEndpoints(sparkInstance, "/shutdown", this::stop);
        sparkInstance.awaitInitialization();
        started = true;
    }

    public void stop() {
        Preconditions.checkState(started, "server not started yet");
        sparkInstance.stop();
        started = false;
    }

    public boolean stopped() {
        return !started;
    }

    public boolean started() {
        return started;
    }


}



