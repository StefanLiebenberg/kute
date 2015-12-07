package slieb.kute.resources.providers;

import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipFile;


public abstract class AbstractURLResourceProvider implements ResourceProvider<Resource.InputStreaming> {

    private static final String PROTOCOL_ERROR = "Cannot produce ResourceProvider from protocol %s", FILE_ERROR =
            "Cannot produce ResourceProvider from protocol %s";

    @Override
    public Optional<Resource.InputStreaming> getResourceByName(String path) {
        return providerStream().map(s -> s.getResourceByName(path)).filter(Optional::isPresent).map(
                Optional::get).findFirst();
    }

    @Override
    public Stream<Resource.InputStreaming> stream() {
        return providerStream().flatMap(ResourceProvider::stream);
    }

    public Stream<ResourceProvider<Resource.InputStreaming>> providerStream() {
        return urlStream().map(this::createResourceFromUrl).filter(Optional::isPresent).map(Optional::get);
    }

    protected abstract Stream<URL> urlStream();

    protected Optional<ResourceProvider<Resource.InputStreaming>> createResourceFromUrl(URL url) {
        String protocol = url.getProtocol();
        switch (protocol) {
            case "file":
                return createResourceFromFile(new File(url.getFile()));
            default:
                throw new IllegalStateException(String.format(PROTOCOL_ERROR, protocol));
        }
    }

    protected Optional<ResourceProvider<Resource.InputStreaming>> createResourceFromFile(File file) {
        if (!file.exists()) return Optional.empty(); // this could be a empty/non-existent directory

        if (file.getPath().endsWith(".jar")) {
            try {
                return Optional.of(new ZipFileResourceProvider(new ZipFile(file)));
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        }

        if (file.isDirectory()) {
            return Optional.of(new FileResourceProvider(file));
        }

        throw new IllegalStateException(String.format(FILE_ERROR, file.toString()));
    }
}
