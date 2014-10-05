package slieb.kute.resources;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Resources {
    private Resources() {
    }

    private static String slurp(Reader reader, int bufferSize) throws IOException {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        for (; ; ) {
            int rsz = reader.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }


    public static String readResource(Resource.Readable resource) throws IOException {
        try (Reader reader = resource.getReader()) {
            return slurp(reader, 1024);
        }
    }

    public static void writeResource(Resource.Writeable resource, String content) throws IOException {
        try (Writer writer = resource.getWriter()) {
            writer.write(content);
        }
    }

    public static void copyResource(Resource.Readable readable, Resource.Writeable writeable) throws IOException {
        try (Reader reader = readable.getReader(); Writer writer = writeable.getWriter()) {
            writer.write(slurp(reader, 1024));
        }
    }
}
