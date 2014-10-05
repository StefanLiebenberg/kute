package slieb.kute.resources.special;

import slieb.kute.resources.Resource;
import slieb.kute.resources.Resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class LazyFileProvider {

    private final static File directory = getDirectory();

    private static File getDirectory() {
        try {
            Path path = Files.createTempDirectory(LazyFileProvider.class.getName());
            File dir = path.toFile();
            dir.deleteOnExit();
            return dir;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private final Resource.Readable resource;

    public LazyFileProvider(Resource.Readable resource) {
        this.resource = resource;
    }

    private File file;

    public File getFile() throws IOException {
        if (file == null) {
            file = new File(directory, resource.getPath());
            createTemporaryPath(file.getParentFile());
            if (!file.exists()) {
                try (Writer writer = new FileWriter(file)) {
                    writer.write(Resources.readResource(resource));
                }
            }
            file.deleteOnExit();
        }
        return file;
    }

    private static void createTemporaryPath(File file) {
        if (file != null && file != directory && !file.exists()) {
            createTemporaryPath(file.getParentFile());
            if (file.mkdir()) {
                file.deleteOnExit();
            }
        }
    }
}
