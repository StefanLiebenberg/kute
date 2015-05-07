package slieb.kute.api;

import java.io.*;

import static slieb.kute.resources.Resources.getResourceAs;

/**
 * The Resource class represents a entry on the class path.
 * <p>
 * Resources can also implement one of the interfaces @code {Readable} or @code {Writeable}
 */
public interface Resource extends Serializable {

    /**
     * The path to this classpath item.
     *
     * @return The resource path.
     */
    String getPath();


    /**
     * The readable version of Resource. Includes a getReader method.
     */
    interface Readable extends Resource {
        /**
         * @return A reader that will give you the contents of resource.
         * @throws IOException an IO exception.
         */
        Reader getReader() throws IOException;

    }


    /**
     * The readable version of Resource. Includes a getWriter method.
     */
    interface Writeable extends Resource {

        /**
         * @return A writer that will write to resource.g
         * @throws IOException an IO exception.
         */
        Writer getWriter() throws IOException;

    }

    /**
     *
     */
    interface InputStreaming extends Resource.Readable {

        @Override
        default Reader getReader() throws IOException {
            return new InputStreamReader(getInputStream());
        }

        InputStream getInputStream() throws IOException;
    }

    /**
     *
     */
    interface OutputStreaming extends Resource.Writeable {

        @Override
        default Writer getWriter() throws IOException {
            return new OutputStreamWriter(getOutputStream());
        }

        OutputStream getOutputStream() throws IOException;
    }

    /**
     * @param <A>
     */
    interface Proxy<A extends Resource> extends Resource, Readable, Writeable, OutputStreaming, InputStreaming {

        A getResource();

        @Override
        default Reader getReader() throws IOException {
            return getResourceAs(getResource(), Readable.class).getReader();
        }

        @Override
        default Writer getWriter() throws IOException {
            return getResourceAs(getResource(), Writeable.class).getWriter();
        }

        @Override
        default InputStream getInputStream() throws IOException {
            return getResourceAs(getResource(), InputStreaming.class).getInputStream();
        }

        @Override
        default OutputStream getOutputStream() throws IOException {
            return getResourceAs(getResource(), OutputStreaming.class).getOutputStream();
        }
    }

}
