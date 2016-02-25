package org.slieb.kute.providers;

import com.google.common.base.Preconditions;
import org.slieb.kute.resources.FileResource;
import org.slieb.kute.api.Resource;

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

    public DirectoryProvider(File directory) {
        this.directory = directory;
    }

    private File getFileForPath(String path) {
        return new File(directory, path);
    }

    private boolean canProvideDirectory() {
        return directory.exists() && directory.canRead();
    }

    private Stream<Resource.Readable> streamWithIO() throws IOException {
        if (canProvideDirectory()) {
            return Files.walk(directory.toPath())
                        .map(Path::toFile)
                        .filter(this::shouldProvideFile)
                        .map(this::createFileResource);
        } else {
            return Stream.empty();
        }
    }

    @Override
    public Stream<Resource.Readable> stream() {
        try {
            return distinctPath(streamWithIO());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        if (canProvideDirectory()) {
            return Optional.of(getFileForPath(path))
                           .filter(this::shouldProvideFile)
                           .map(file -> fileResource(path, file));
        } else {
            return Optional.empty();
        }
    }

    private boolean shouldProvideFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    private FileResource createFileResource(File file) {
        String rootPath = directory.getAbsolutePath();
        String path = file.getAbsolutePath();
        Preconditions.checkState(path.startsWith(rootPath));
        return fileResource(path.substring(rootPath.length()), file);
    }

    @Override
    public Resource.Writable create(String path) {
        return createFileResource(getFileForPath(path));
    }
}
