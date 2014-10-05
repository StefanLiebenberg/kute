package slieb.kute.resources;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * The Resource class represents a entry on the class path.
 *
 * Resources can also implement one of the interfaces @code {Readable} or @code {Writeable}
 *
 *
 */
public interface Resource extends Serializable {

    /**
     * The path to this classpath item.
     *
     * @return
     */
    public String getPath();

    /**
     * The readable version of Resource. Includes a getReader method.
     */
    public static interface Readable extends Resource {
        public Reader getReader() throws IOException;
    }

    /**
     * The readable version of Resource. Includes a getWriter method.
     */
    public static interface Writeable extends Resource {
        public Writer getWriter() throws IOException;
    }
}
