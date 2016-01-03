package slieb.kute.providers;

import slieb.kute.api.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipFile;


public abstract class AbstractURLResourceProvider implements Resource.Provider {

    private static final String PROTOCOL_ERROR = "Cannot produce ResourceProvider from protocol %s", FILE_ERROR =
            "Cannot produce ResourceProvider from protocol %s";

    @Override
    public Stream<Resource.Readable> stream() {
        return providerStream().flatMap(Resource.Provider::stream);
    }

    public Stream<Resource.Provider> providerStream() {
        return urlStream().map(this::createResourceFromUrl).filter(Optional::isPresent).map(Optional::get);
    }

    protected abstract Stream<URL> urlStream();

    protected Optional<Resource.Provider> createResourceFromUrl(URL url) {
        String protocol = url.getProtocol();
        switch (protocol) {
            case "file":
                return createResourceFromFile(new File(url.getFile()));
            default:
                throw new IllegalStateException(String.format(PROTOCOL_ERROR, protocol));
        }
    }

    protected Optional<Resource.Provider> createResourceFromFile(File file) {
        if (!file.exists()) return Optional.empty(); // this could be a empty/non-existent directory

        if (file.getPath().endsWith(".jar")) {
            try {
                return Optional.of(new ZipFileResourceProvider(new ZipFile(file)));
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        }

        if (file.isDirectory()) {
            //noinspection unchecked
            return Optional.of(new FileResourceProvider(file));
        }

        throw new IllegalStateException(String.format(FILE_ERROR, file.toString()));
    }
}
