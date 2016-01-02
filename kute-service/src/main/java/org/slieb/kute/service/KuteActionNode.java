package org.slieb.kute.service;


import org.slieb.kute.service.annotations.KuteAction;
import spark.Filter;
import spark.Route;
import spark.SparkInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class KuteActionNode implements Consumer<SparkInstance> {

    private final Object controller;
    private final Method method;

    private final Method[] beforeMethods, afterMethods;

    public KuteActionNode(final Object controller,
                          final Method method,
                          final Method[] beforeMethods,
                          final Method[] afterMethods) {
        this.controller = controller;
        this.method = method;
        this.beforeMethods = beforeMethods;
        this.afterMethods = afterMethods;
    }

    @Override
    public void accept(SparkInstance sparkInstance) {

        KuteAction kuteAction = method.getAnnotation(KuteAction.class);
        for (Method beforeMethod : beforeMethods) {
            sparkInstance.before(kuteAction.value(), getActionFilter(beforeMethod));
        }
        for (Method afterMethod : afterMethods) {
            sparkInstance.after(kuteAction.value(), getActionFilter(afterMethod));
        }
        for (KuteAction.Method httpMethod : kuteAction.methods()) {
            switch (httpMethod) {
                case GET:
                    sparkInstance.get(kuteAction.value(), getActionRoute(method));
                    break;
                case POST:
                    sparkInstance.post(kuteAction.value(), getActionRoute(method));
                    break;
            }
        }
    }

    public Filter getActionFilter(final Method filterMethod) {
        return (request, response) -> {
            try {
                filterMethod.invoke(controller, resolveArguments(filterMethod, Stream.of(request, response).collect(toList())));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        };
    }


    public Route getActionRoute(final Method actionMethod) {
        return ((request, response) -> {
            try {
                return actionMethod.invoke(controller,
                        resolveArguments(actionMethod, Stream.of(request, response).collect(toList())));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        });
    }


    private Object[] resolveArguments(Method filterMethod,
                                      List<?> objects) {
        return Arrays.asList(filterMethod.getParameterTypes()).stream()
                .map(p -> objects.stream().filter(p::isInstance).findFirst().orElse(null))
                .toArray(Object[]::new);
    }
}
