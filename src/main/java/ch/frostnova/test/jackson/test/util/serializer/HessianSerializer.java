package ch.frostnova.test.jackson.test.util.serializer;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.ExtSerializerFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static ch.frostnova.test.jackson.test.util.util.Unchecked.unchecked;

public class HessianSerializer<T> implements Serializer<T> {

    private static final SerializerFactory serializerFactory = new SerializerFactory();
    private final Class<T> type;

    static {
        var extSerializerFactory = new ExtSerializerFactory();
        extSerializerFactory.addSerializer(Instant.class, new InstantSerializer());
        extSerializerFactory.addDeserializer(Instant.class, new InstantDeserializer());
        extSerializerFactory.addSerializer(LocalDate.class, new LocalDateSerializer());
        extSerializerFactory.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        extSerializerFactory.addSerializer(Duration.class, new DurationSerializer());
        extSerializerFactory.addDeserializer(Duration.class, new DurationDeserializer());
        serializerFactory.addFactory(extSerializerFactory);
    }

    public HessianSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(Object value) {
        return unchecked(() -> {
            var byteOut = new ByteArrayOutputStream();
            var objectOut = new Hessian2Output(byteOut);
            objectOut.setSerializerFactory(serializerFactory);
            objectOut.writeObject(value);
            objectOut.flush();
            objectOut.close();
            return byteOut.toByteArray();
        });
    }

    @Override
    public T deserialize(byte[] serialized) {
        return unchecked(() -> {
            var byteIn = new ByteArrayInputStream(serialized);
            var objectIn = new Hessian2Input(byteIn);
            objectIn.setSerializerFactory(serializerFactory);
            return type.cast(objectIn.readObject());
        });
    }

    public static class InstantSerializer extends AbstractSerializer {
        @Override
        public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
            if (obj instanceof Instant instant) {
                out.writeObject(instant.toEpochMilli());
            } else {
                out.writeNull();
            }
        }
    }

    public static class InstantDeserializer extends AbstractDeserializer {
        @Override
        public Object readObject(AbstractHessianInput in) throws IOException {
            Object obj = in.readObject();
            if (obj instanceof Long epochMilli) {
                return Instant.ofEpochMilli(epochMilli);
            }
            return null;
        }
    }

    public static class LocalDateSerializer extends AbstractSerializer {
        @Override
        public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
            if (obj instanceof LocalDate localDate) {
                out.writeObject(localDate.toString());
            } else {
                out.writeNull();
            }
        }
    }

    public static class LocalDateDeserializer extends AbstractDeserializer {
        @Override
        public Object readObject(AbstractHessianInput in) throws IOException {
            Object obj = in.readObject();
            if (obj instanceof String string) {
                return LocalDate.parse(string);
            }
            return null;
        }
    }

    public static class DurationSerializer extends AbstractSerializer {
        @Override
        public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
            if (obj instanceof Duration duration) {
                out.writeObject(duration.toString());
            } else {
                out.writeNull();
            }
        }
    }

    public static class DurationDeserializer extends AbstractDeserializer {
        @Override
        public Object readObject(AbstractHessianInput in) throws IOException {
            Object obj = in.readObject();
            if (obj instanceof String string) {
                return Duration.parse(string);
            }
            return null;
        }
    }
}
