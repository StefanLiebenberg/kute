package org.slieb.kute.service;


import spark.SparkInstance;

import java.util.Collection;
import java.util.function.Consumer;

public class KuteControllerNode implements Consumer<SparkInstance> {

    private Collection<KuteActionNode> actionNodes;

    public KuteControllerNode(Collection<KuteActionNode> actionNodes) {
        this.actionNodes = actionNodes;
    }

    public Collection<KuteActionNode> getActions() {
        return actionNodes;
    }

    @Override
    public void accept(SparkInstance sparkInstance) {
        actionNodes.forEach(node -> node.accept(sparkInstance));
    }
}
