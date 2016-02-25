package org.slieb.kute;

import org.slieb.throwables.SupplierWithThrowable;
import org.slieb.kute.api.Resource;
import org.slieb.kute.resources.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class KuteFactory {

    public static URLResource urlResource(final String path,
                                          final URL url) {
        return new URLResource(path, url);
    }

    public static RenamedPathResource zipEntryResource(String path,
                                                       ZipFile zipFile,
                                                       ZipEntry zipEntry) {
        return Kute.renameResource(path, zipEntryResource(zipFile, zipEntry));
    }

    public static Resource.Readable zipEntryResource(final ZipFile zipFile,
                                                     ZipEntry zipEntry) {
        return inputStreamResource(zipEntry.getName(), () -> zipFile.getInputStream(zipEntry));
    }

    /**
     * Create resources from input stream.
     *
     * @param path     The path name for this resource.
     * @param supplier The input stream to supply content.
     * @return a readable resource that reads from provided input stream.
     */
    public static InputStreamResource inputStreamResource(final String path,
                                                          final SupplierWithThrowable<InputStream, IOException> supplier) {
        return new InputStreamResource(path, supplier);
    }

    /**
     * Create resources from input stream.
     *
     * @param supplier The input stream to supply content.
     * @param path     The path name for this resource.
     * @return a readable resource that reads from provided input stream.
     */
    public static OutputStreamResource outputStreamResource(final String path,
                                                            final SupplierWithThrowable<OutputStream, IOException> supplier) {
        return new OutputStreamResource(path, supplier);
    }

    public static Resource resource(String path) {
        return () -> path;
    }

    public static MutableBytesArrayResource mutableResource(String s,
                                                            String s1) {
        return new MutableBytesArrayResource(s, s1.getBytes(Charset.defaultCharset()));
    }
}
