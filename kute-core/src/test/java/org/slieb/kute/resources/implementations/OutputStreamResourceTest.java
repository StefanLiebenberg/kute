package org.slieb.kute.resources.implementations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slieb.kute.resources.OutputStreamResource;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.slieb.kute.KuteIO.writeResourceWithOutputStream;


@RunWith(MockitoJUnitRunner.class)
public class OutputStreamResourceTest {


    OutputStreamResource outputStreamResource;

    @Mock
    private Supplier<OutputStream> mockSupplier;

    @Before
    public void setUp() throws Exception {
        outputStreamResource = new OutputStreamResource("/output", mockSupplier::get);
    }


    @Test
    public void testGetPath() throws Exception {
        assertEquals("/output", outputStreamResource.getPath());
    }

    @Test
    public void testGetOutputStreamProxiesToSupplier() throws Exception {
        outputStreamResource.getOutputStream();
        verify(mockSupplier).get();
    }

    @Test
    public void testGetOutputStream() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(mockSupplier.get()).thenReturn(outputStream);
        writeResourceWithOutputStream(outputStreamResource, "write content");
        assertEquals("write content", outputStream.toString(Charset.defaultCharset().name()));
    }


}