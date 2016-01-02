package slieb.kute.resources;

import slieb.kute.api.Resource;

import java.io.*;
import java.util.Objects;

/**
 * <p>A Resource Object to represent file objects. The FileResource is readable and writable and supports getInputStream
 * and getOutputStream. It stores a file object and a path.</p>
 * <p><b>To read file Contents:</b></p>
 * <pre><code>
 * FileResource resource =  new FileResource(file);
 * String content = Kute.readResource(resource);
 * </code></pre>
 * <p><b>To write file Contents:</b></p>
 * <pre><code>
 * FileResource resource =  new FileResource(file);
 * Kute.writeResource(resource, "content");
 * </code></pre>
 * <p><b>To write a binary using outputStream instead.</b></p>
 * <pre><code>
 * FileResource  resource = new FileResource(file);
 * try(OutputStream outputStream = resource.getOutputStream()) {
 * //... do output stream stuff.
 * }
 * </code></pre>
 * <p><b>To Read a binary using InputStream instead.</b></p>
 * <pre><code>
 * FileResource  resource = new FileResource(file);
 * try(InputStream inputStream = resource.getInputStream()) {
 * //... do input stream stuff.
 * }
 * </code></pre>
 */
public class FileResource implements Resource.Readable, Resource.Writable {

    private final String path;

    private final File file;


    /**
     * Specify a file and the path it will be available as.
     *
     * @param file The resource's file object.
     * @param path The path under which this resource will be available as.
     */
    public FileResource(final String path,
                        final File file) {
        this.file = file;
        this.path = path;
    }


    /**
     * This constructor extracts the resource path from file.getPath().
     *
     * @param file The file resource to use.
     */
    public FileResource(File file) {
        this(file.getPath(), file);
    }


    /**
     * @return The resource's file object.
     */
    public File getFile() {
        return file;
    }

    /**
     * @return a FileReader that will read the file object of this resource.
     * @throws IOException when there is a error creating the reader.
     */
    @Override
    public FileReader getReader() throws IOException {
        return new FileReader(this.file);
    }

    /**
     * @return a FileWriter that will writer to the file object of this resource.
     * @throws IOException when there is a error creating the writer.
     */
    @Override
    public FileWriter getWriter() throws IOException {
        this.parentExistsOrMkdirs();
        return new FileWriter(this.file);
    }

    /**
     * @return A FileOutputStream that will will write into the resource's file object.
     * @throws IOException when there is an error creating the output stream.
     */
    @Override
    public FileOutputStream getOutputStream() throws IOException {
        this.parentExistsOrMkdirs();
        return new FileOutputStream(this.file);
    }

    /**
     * @return A FileInputStream that will read from the resource's file object.
     * @throws IOException when there is an error creating the inputStream.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public boolean parentExistsOrMkdirs() {
        final File parentFile = this.file.getParentFile();
        return parentFile != null && (parentFile.exists() || parentFile.mkdirs());
    }


    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "path='" + path + '\'' +
                ", file=" + file +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileResource)) return false;
        FileResource that = (FileResource) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, file);
    }
}
