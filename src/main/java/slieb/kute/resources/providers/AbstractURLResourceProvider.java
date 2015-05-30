package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.Kute;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;
import java.util.zip.ZipFile;


public abstract class AbstractURLResourceProvider implements ResourceProvider<Resource.InputStreaming> {

    private static final String PROTOCOL_ERROR = "Cannot produce ResourceProvider from protocol %s",
            FILE_ERROR = "Cannot produce ResourceProvider from protocol %s";

    @Override
    public Resource.InputStreaming getResourceByName(String path) {
        return Kute.findFirstResource(providerStream().map(s -> s.getResourceByName(path)));
    }

    @Override
    public Stream<Resource.InputStreaming> stream() {
        return providerStream().flatMap(ResourceProvider::stream);
    }

    public Stream<ResourceProvider<? extends Resource.InputStreaming>> providerStream() {
        return urlStream().map(this::createResourceFromUrl).filter(p -> p != null);
    }

    protected abstract Stream<URL> urlStream();

    protected ResourceProvider<? extends Resource.InputStreaming> createResourceFromUrl(URL url) {
        String protocol = url.getProtocol();
        switch (protocol) {
            case "file":
                return createResourceFromFile(new File(url.getFile()));
            default:
                throw new IllegalStateException(String.format(PROTOCOL_ERROR, protocol));
        }
    }

    protected ResourceProvider<? extends Resource.InputStreaming> createResourceFromFile(File file) {
        if (!file.exists()) return null; // this could be a empty/non-existent directory

        if (file.getPath().endsWith(".jar")) {
            try {
                return new ZipFileResourceProvider(new ZipFile(file));
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        }

        if (file.isDirectory()) {
            return new FileResourceProvider(file);
        }

        throw new IllegalStateException(String.format(FILE_ERROR, file.toString()));
    }
}
