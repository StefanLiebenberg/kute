package slieb.kute.api;


import java.io.IOException;
import java.io.Serializable;

public interface ConsumerWithIO<T> extends Serializable {
    void acceptWithIO(T object) throws IOException;
}
