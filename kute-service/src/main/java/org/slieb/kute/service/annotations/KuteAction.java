package org.slieb.kute.service.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface KuteAction {
    String value();

    Method[] methods() default {Method.GET};


    enum Method {
        GET, POST
    }


}
