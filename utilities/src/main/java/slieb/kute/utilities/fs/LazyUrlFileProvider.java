package slieb.kute.utilities.fs;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class LazyUrlFileProvider {


    private static File getDirectory() {
        try {
            Path path = Files.createTempDirectory(LazyUrlFileProvider.class.getName());
            File dir = path.toFile();
            dir.deleteOnExit();
            return dir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final static File directory = getDirectory();


    private final URL url;

    private File file;

    public LazyUrlFileProvider(URL url) {
        this.url = url;
    }

    public File getFile() throws IOException {
        if (file == null) {
            createFile(file, url);
        }
        return file;
    }

    private static void createFile(File file, URL url) throws IOException {
        file = new File(directory, url.getPath());
        createTemporaryPath(file.getParentFile());
        if (!file.exists()) {
            try (Writer writer = new FileWriter(file); InputStream iostream = url.openStream()) {
                IOUtils.copy(iostream, writer);
            }
        }
        file.deleteOnExit();
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
