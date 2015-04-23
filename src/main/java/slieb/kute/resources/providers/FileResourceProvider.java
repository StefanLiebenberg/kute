package slieb.kute.resources.providers;

import com.google.common.base.Preconditions;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.Resources;
import slieb.kute.resources.implementations.FileResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileResourceProvider implements ResourceProvider<FileResource> {

    public final File directory;

    public FileResourceProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public FileResource getResourceByName(String path) {
        File file = new File(directory, path);
        if (file.exists()) {
            return Resources.fileResource(file, path);
        } else {
            return null;
        }
    }

    @Override
    public Stream<FileResource> stream() {
        try {
            return Files.walk(directory.toPath()).map(Path::toString).map(File::new).map(this::createFileResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileResource createFileResource(File file) {
        String rootPath = directory.getAbsolutePath();
        String path = file.getAbsolutePath();
        Preconditions.checkState(path.startsWith(rootPath));
        return new FileResource(file, path.substring(rootPath.length()));

    }


}
