package org.slieb.kute.service;

import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class KuteServlet extends HttpServlet {

    private final KuteServiceBeta beta;

    public KuteServlet(KuteServiceBeta beta) {
        this.beta = beta;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!beta.handle(req, resp, HttpMethod.GET)) {
            super.doGet(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!beta.handle(req, resp, HttpMethod.POST)) {
            super.doPost(req, resp);
        }
    }
}
