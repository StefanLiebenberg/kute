package org.slieb.kute.providers;

import com.google.common.base.Preconditions;
import org.slieb.kute.api.Resource;
import org.slieb.kute.resources.FileResource;
import org.slieb.throwables.FunctionWithThrowable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static org.slieb.kute.Kute.distinctPath;
import static org.slieb.kute.Kute.fileResource;

public final class DirectoryProvider implements Resource.Provider, Resource.Creator {

    public final File directory;

    public DirectoryProvider(final File directory) {
        this.directory = directory;
    }

    private boolean shouldProvideFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    private Stream<Resource.Readable> getStream(final FunctionWithThrowable<File, Stream<File>, IOException> directoryFunction) {
        return distinctPath(Stream.of(directory)
                                  .filter((dir) -> dir.exists() && dir.canRead())
                                  .flatMap(directoryFunction)
                                  .filter(this::shouldProvideFile)
                                  .map(this::createFileResource));
    }

    @Override
    public Stream<Resource.Readable> stream() {
        return getStream(dir -> Files.walk(dir.toPath()).map(Path::toFile));
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        return getStream(dir -> Stream.of(new File(dir, path))).findFirst();
    }

    private FileResource createFileResource(File file) {
        String rootPath = directory.getAbsolutePath();
        String path = file.getAbsolutePath();
        Preconditions.checkState(path.startsWith(rootPath));
        return fileResource(path.substring(rootPath.length()), file);
    }

    @Override
    public Resource.Writable create(String path) {
        return createFileResource(new File(directory, path));
    }
}
