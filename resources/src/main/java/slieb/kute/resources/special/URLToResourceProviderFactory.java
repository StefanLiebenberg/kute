package slieb.kute.resources.special;


import slieb.kute.resources.Resource;
import slieb.kute.resources.ResourceProvider;
import slieb.kute.resources.providers.FileResourceProvider;
import slieb.kute.resources.providers.ZipFileResourceProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

public class URLToResourceProviderFactory {

    public ResourceProvider<? extends Resource.Readable> create(URL url) throws IOException {

        if (url.getPath().endsWith(".jar")) {
            return createJarResourceProvider(url);
        }

        File file = new File(url.getPath());
        if (file.exists() && file.isDirectory()) {
            return createDirectoryResourceProvider(file);
        }

        throw new IllegalStateException("Unknown url type. " + url.toString());
    }

    public FileResourceProvider createDirectoryResourceProvider(File directory) throws IOException {
        return new FileResourceProvider(directory);
    }


    public ZipFileResourceProvider createJarResourceProvider(URL url) throws IOException {
        JarFile jarFile = new JarFile(url.getPath());
        return new ZipFileResourceProvider(jarFile);
    }
}
