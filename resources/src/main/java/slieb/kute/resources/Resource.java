package slieb.kute.resources;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * The Resource class represents a entry on the class path.
 * <p/>
 * Resources can also implement one of the interfaces @code {Readable} or @code {Writeable}
 */
public interface Resource extends Serializable {

    /**
     * The path to this classpath item.
     *
     * @return The resource path.
     */
    public String getPath();

    /**
     * The readable version of Resource. Includes a getReader method.
     */
    public static interface Readable extends Resource {
        /**
         * @return A reader that will give you the contents of resource.
         * @throws IOException
         */
        public Reader getReader() throws IOException;
    }

    /**
     * The readable version of Resource. Includes a getWriter method.
     */
    public static interface Writeable extends Resource {

        /**
         * @return A writer that will write to resource.
         * @throws IOException
         */
        public Writer getWriter() throws IOException;
    }
}
