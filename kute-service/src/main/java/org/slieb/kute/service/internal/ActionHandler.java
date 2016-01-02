package org.slieb.kute.service.internal;


import com.google.common.collect.ImmutableList;
import spark.Request;
import spark.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ActionHandler implements Handler {

    private final Object controller;

    private final Method method;

    public ActionHandler(Object controller,
                         Method method) {
        this.method = method;
        this.controller = controller;
    }

    @Override
    public Optional handle(Request request,
                           Response response,
                           Optional previous) {
        try {
            List<?> objects = ImmutableList.of(request, response, previous);
            Object[] arguments = Arrays.asList(method.getParameterTypes()).stream()
                    .map(p -> objects.stream().filter(p::isInstance).findFirst().orElse(null))
                    .toArray(Object[]::new);
            return Optional.ofNullable(method.invoke(controller, arguments));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

}
