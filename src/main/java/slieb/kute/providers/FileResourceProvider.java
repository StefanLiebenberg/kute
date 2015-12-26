package slieb.kute.providers;

import com.google.common.base.Preconditions;
import slieb.kute.utils.KuteLambdas;
import slieb.kute.api.Resource;
import slieb.kute.resources.FileResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static slieb.kute.Kute.fileResource;

public final class FileResourceProvider implements Resource.Provider, Resource.Creator {

    public final File directory;

    public FileResourceProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public Optional<Resource.Readable> getResourceByName(String path) {
        if (canProvideDirectory()) {
            return Optional.of(getFileInDirectory(path))
                    .filter(this::shouldProvideFile)
                    .map(file -> fileResource(path, file));
        }
        return Optional.empty();
    }

    @Override
    public Stream<Resource.Readable> stream() {
        if (canProvideDirectory()) {
            return KuteLambdas.safelySupply(this::streamInternal).get();
        } else {
            return Stream.empty();
        }
    }

    private Stream<Resource.Readable> streamInternal() throws IOException {
        return Files.walk(directory.toPath())
                .map(Path::toString)
                .map(File::new)
                .filter(this::shouldProvideFile)
                .map(this::createFileResource);
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

    @Override
    public Resource.Writable create(String path) {
        return createFileResource(getFileInDirectory(path));
    }

    private File getFileInDirectory(String path) {
        return new File(directory, path);
    }

}
