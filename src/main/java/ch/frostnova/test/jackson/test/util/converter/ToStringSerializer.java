package ch.frostnova.test.jackson.test.util.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Serializer for value objects (using <code>toString()</code>).
 *
 * @author pwalser
 * @since 06.07.2018
 */
public final class ToStringSerializer extends StdSerializer<Object> {

    public ToStringSerializer() {
        super(Object.class);
    }


    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (value == null) {
            generator.writeNull();
        } else {
            generator.writeString(value.toString());
        }
    }
}