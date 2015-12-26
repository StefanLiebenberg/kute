package slieb.kute.resources.implementations;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.kute.resources.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static slieb.kute.utils.KuteIO.readResourceWithInputStream;

@RunWith(MockitoJUnitRunner.class)
public class InputStreamResourceTest {

    InputStreamResource outputStreamResource;

    @Mock
    private Supplier<InputStream> mockSupplier;

    @Before
    public void setUp() throws Exception {
        outputStreamResource = new InputStreamResource("/output", mockSupplier);
    }


    @Test
    public void testGetPath() throws Exception {
        assertEquals("/output", outputStreamResource.getPath());
    }

    @Test
    public void testGetOutputStreamProxiesToSupplier() throws Exception {
        outputStreamResource.getInputStream();
        verify(mockSupplier).get();
    }

    @Test
    public void testGetOutputStream() throws Exception {
        when(mockSupplier.get()).thenReturn(new ByteArrayInputStream("input content".getBytes()));
        assertEquals("input content", readResourceWithInputStream(outputStreamResource));
    }


}
