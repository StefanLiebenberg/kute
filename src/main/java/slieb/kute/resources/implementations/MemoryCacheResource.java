package slieb.kute.resources.implementations;


import slieb.kute.api.Resource;

import java.io.IOException;
import java.io.Reader;

import static slieb.kute.resources.Resources.readResource;

public class MemoryCacheResource implements Resource.Readable {

    private final Readable readable;
    private StringResource stringResource;

    public MemoryCacheResource(Readable readable) {
        this.readable = readable;
    }


    @Override
    public Reader getReader() throws IOException {
        if (stringResource == null) {
            this.stringResource = new StringResource(readResource(readable), readable.getPath());
        }
        return stringResource.getReader();
    }

    @Override
    public String getPath() {
        return readable.getPath();
    }
}
