package org.slieb.kute.service;


import com.google.common.base.Preconditions;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import spark.Spark;
import spark.SparkInstance;

import static org.slieb.sparks.Sparks.addServerStopEndpoints;

/**
 *
 */
public class KuteService {

    private boolean started = false;

    private final KuteFactory factory;

    private final SparkInstance sparkInstance;


    public KuteService(SparkInstance sparkInstance) {
        this.sparkInstance = sparkInstance;
        this.factory = new KuteFactory();
    }

    public KuteService() {
        this(Spark.getInstance());
    }


    public void addController(Object controller) {
        KuteControllerNode node = factory.getControllerNode(controller);
        node.accept(sparkInstance);
    }

    public void addResourceProvider(ResourceProvider<? extends Resource.Readable> resourceProvider) {
        sparkInstance.get("/*", new ResourcesRoute(resourceProvider));
    }

    public void start() throws InterruptedException {
        Preconditions.checkState(!started, "Server already started");
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



