package org.slieb.kute.service;


import org.slieb.kute.service.annotations.KuteAction;
import org.slieb.kute.service.annotations.KuteAfter;
import org.slieb.kute.service.annotations.KuteBefore;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class KuteFactory {

    public KuteControllerNode getControllerNode(Object controllerKlass) {
        return createControllerFunction(controllerKlass.getClass()).apply(controllerKlass);
    }

    public Function<Object, KuteControllerNode> createControllerFunction(Class klass) {
        Function<Object, Collection<KuteActionNode>> nodesFunction = createActionNodesFunction(klass);
        return (controller) -> new KuteControllerNode(nodesFunction.apply(controller));
    }


    public Function<Object, Collection<KuteActionNode>> createActionNodesFunction(Class klass) {
        List<Method> list = Arrays.asList(klass.getMethods()).stream().filter(
                method -> method.isAnnotationPresent(KuteAction.class)).collect(toList());
        List<Method> beforeMethods = Arrays.asList(klass.getMethods()).stream().filter(
                method -> method.isAnnotationPresent(KuteBefore.class)).collect(Collectors.toList());
        List<Method> afterMethods = Arrays.asList(klass.getMethods()).stream().filter(
                method -> method.isAnnotationPresent(KuteAfter.class)).collect(Collectors.toList());
        return (controller) -> {
            return list.stream().map(method -> {

                Method[] beforeFilters = beforeMethods.stream().filter(beforeMethod -> {
                    KuteBefore kuteBefore = beforeMethod.getAnnotation(KuteBefore.class);
                    return (kuteBefore.only().length == 0 || Arrays.asList(kuteBefore.only()).contains(
                            method.getName()));
                }).toArray(Method[]::new);

                Method[] afterFilters = afterMethods.stream().filter(afterMethod -> {
                    KuteAfter kuteAfter = afterMethod.getAnnotation(KuteAfter.class);
                    return (kuteAfter.only().length == 0 || Arrays.asList(kuteAfter.only()).contains(
                            method.getName()));
                }).toArray(Method[]::new);

                return new KuteActionNode(controller, method, beforeFilters, afterFilters);
            }).collect(toList());
        };
    }

}
