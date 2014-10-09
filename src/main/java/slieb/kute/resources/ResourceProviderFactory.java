package slieb.kute.resources;


import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.providers.FileResourceProvider;
import slieb.kute.resources.providers.ZipFileResourceProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class ResourceProviderFactory {

    private ResourceProviderFactory() {
    }


    public static ResourceProvider<? extends Resource.Readable> create(URL url) throws IOException {

        File urlFile = new File(url.getFile());
        if (urlFile.exists()) {
            if (urlFile.isDirectory()) {
                return createFileResourceProvider(urlFile);
            }

            String loweredPath = urlFile.getPath().toLowerCase();
            if (loweredPath.endsWith(".jar")) {
                return createZipFileResourceProvider(new JarFile(urlFile));
            }
        }
        throw new IllegalStateException("Unknown url type. " + url.toString());
    }


    public static FileResourceProvider createFileResourceProvider(File directory) {
        return new FileResourceProvider(directory);
    }

    public static ZipFileResourceProvider createZipFileResourceProvider(ZipFile zipFile) {
        return new ZipFileResourceProvider(zipFile);
    }
}
