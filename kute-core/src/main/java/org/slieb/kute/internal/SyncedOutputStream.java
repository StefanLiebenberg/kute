package org.slieb.kute.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

public class SyncedOutputStream extends OutputStream {

    private final AtomicReference<byte[]> bytes;

    private final ByteArrayOutputStream bufferOutputStream;

    public SyncedOutputStream(final AtomicReference<byte[]> bytes) {
        this.bytes = bytes;
        this.bufferOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public final void write(int b) throws IOException {
        bufferOutputStream.write(b);
    }

    @Override
    public final void close() throws IOException {
        bufferOutputStream.close();
        bytes.set(bufferOutputStream.toByteArray());
    }
}
