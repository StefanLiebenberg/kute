package org.slieb.kute.service.internal;


import spark.Request;
import spark.Response;

import java.util.Optional;

@FunctionalInterface
public interface Handler {

    Optional handle(Request request, Response response, Optional previous);

}
