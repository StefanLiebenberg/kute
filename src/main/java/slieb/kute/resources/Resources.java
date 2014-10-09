package slieb.kute.resources;

import org.apache.commons.io.IOUtils;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.implementations.*;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Resources {
    private Resources() {
    }

    public static String readResource(Resource.Readable resource) throws IOException {
        try (Reader reader = resource.getReader()) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            return writer.toString();
        }
    }

    public static void writeResource(Resource.Writeable resource, String content) throws IOException {
        try (Writer writer = resource.getWriter()) {
            IOUtils.write(content, writer);
        }
    }

    public static void copyResource(Resource.Readable readable, Resource.Writeable writeable) throws IOException {
        try (Reader reader = readable.getReader(); Writer writer = writeable.getWriter()) {
            IOUtils.copy(reader, writer);
        }
    }

    public static FileResource fileResource(File file) {
        return new FileResource(file);
    }

    public static FileResource fileResource(File file, String path) {
        return new FileResource(file, path);
    }

    public static <A extends Resource> RenamedPathResource<A> rename(A resource, String path) {
        return new RenamedPathResource<>(resource, path);
    }

    public static InputStreamResource inputStreamResource(InputStream inputStream, String path) {
        return new InputStreamResource(inputStream, path);
    }

    public static MemoryCacheResource cacheResource(Resource.Readable resource) {
        return new MemoryCacheResource(resource);
    }

    public static StringResource stringResource(String content, String path) {
        return new StringResource(content, path);
    }

    public static URLResource urlResource(URL url, String path) {
        return new URLResource(url, path);
    }

    public static ZipEntryResource zipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        return new ZipEntryResource(zipFile, zipEntry);
    }

    public static RenamedPathResource<ZipEntryResource> zipEntryResource(ZipFile zipFile, ZipEntry zipEntry, String path) {
        return rename(zipEntryResource(zipFile, zipEntry), path);
    }


    public static <R extends Resource> Collection<R> collect(ResourceProvider<R> resourceProvider) {
        HashSet<R> collection = new HashSet<>();
        for (R resource : resourceProvider.getResources()) {
            collection.add(resource);
        }
        return collection;
    }
}
