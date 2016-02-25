package org.slieb.kute.resources;

import org.slieb.kute.api.Resource;

import java.io.*;
import java.nio.charset.Charset;

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
public class FileResource extends AbstractResource implements Resource.Readable, Resource.Writable {

    private final File file;

    /**
     * Specify a file and the path it will be available as.
     *
     * @param file The resource's file object.
     * @param path The path under which this resource will be available as.
     */
    public FileResource(final String path,
                        final File file,
                        final Charset charset) {
        super(path, charset);
        this.file = file;
    }

    /**
     * @param path The resource path.
     */
    public FileResource(final String path,
                        final File file) {
        this(path, file, Charset.defaultCharset());
    }

    /**
     * @return The resource's file object.
     */
    public File getFile() {
        return file;
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
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof FileResource)) { return false; }
        if (!super.equals(o)) { return false; }

        final FileResource that = (FileResource) o;

        return file != null ? file.equals(that.file) : that.file == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "file=" + file +
                "} " + super.toString();
    }
}
