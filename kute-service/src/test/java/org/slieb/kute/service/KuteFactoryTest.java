package org.slieb.kute.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slieb.kute.service.annotations.KuteAction;
import org.slieb.kute.service.sample.HelloWorldController;
import spark.Request;
import spark.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KuteFactoryTest {

    private KuteFactory factory;
    private Object controller;

    @Mock
    Request mockRequest;

    @Mock
    Response mockResponse;

    @Before
    public void setUp() throws Exception {
        controller = new HelloWorldController();
        factory = new KuteFactory();
        when(mockRequest.pathInfo()).thenReturn("/greeting");
        when(mockRequest.requestMethod()).thenReturn(KuteAction.Method.GET.name());
    }


    @Test
    public void testGetHandlerSize() {
        KuteControllerNode controllerNode = factory.getControllerNode(controller);
        assertEquals(1, controllerNode.getActions().size());
    }


}

