package slieb.kute.resources.providers;

import com.google.common.base.Preconditions;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.ResourceException;
import slieb.kute.resources.implementations.FileResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static slieb.kute.Kute.fileResource;

public class FileResourceProvider implements ResourceProvider<FileResource> {

    public final File directory;

    public FileResourceProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public Optional<FileResource> getResourceByName(String path) {
        if (canProvideDirectory()) {
            File file = new File(directory, path);
            if (shouldProvideFile(file)) {
                return Optional.of(fileResource(path, file));
            }
        }
        return Optional.empty();
    }

    @Override
    public Stream<FileResource> stream() {
        if (canProvideDirectory()) {
            try {
                return Files.walk(directory.toPath()).map(Path::toString).map(File::new).filter(
                        this::shouldProvideFile).map(this::createFileResource);
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        } else {
            return Stream.empty();
        }
    }

    private boolean canProvideDirectory() {
        return directory.exists() && directory.canRead();
    }

    private boolean shouldProvideFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    private FileResource createFileResource(File file) {
        String rootPath = directory.getAbsolutePath();
        String path = file.getAbsolutePath();
        Preconditions.checkState(path.startsWith(rootPath));
        return new FileResource(path.substring(rootPath.length()), file);
    }


}
