package org.slieb.kute.service;

import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KuteServiceBeta {

    public boolean handle(HttpServletRequest req,
                          HttpServletResponse resp,
                          HttpMethod get) {
        return false;
    }
}
