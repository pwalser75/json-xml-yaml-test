package ch.frostnova.test.jackson.test.util.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static ch.frostnova.test.jackson.test.util.util.Unchecked.unchecked;

public class JavaClassicSerializer<T> implements Serializer<T> {

    private final Class<T> type;

    public JavaClassicSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(Object value) {
        return unchecked(() -> {
            var byteOut = new ByteArrayOutputStream();
            var objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(value);
            objectOut.flush();
            return byteOut.toByteArray();
        });
    }

    @Override
    public T deserialize(byte[] serialized) {
        return unchecked(() -> {
            var byteIn = new ByteArrayInputStream(serialized);
            var objectIn = new ObjectInputStream(byteIn);
            return type.cast(objectIn.readObject());
        });
    }
}
