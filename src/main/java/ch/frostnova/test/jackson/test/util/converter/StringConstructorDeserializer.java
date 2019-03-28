package ch.frostnova.test.jackson.test.util.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Deserializer for value objects (using <code>String</code> constructor: <code>new T(String value)</code>).
 *
 * @author pwalser
 * @since 06.07.2018
 */
public abstract class StringConstructorDeserializer<T> extends StdDeserializer<T> {

    private final Constructor<T> constructor;

    public StringConstructorDeserializer(Class<T> type) {
        super(type);
        if (type == null) {
            throw new IllegalArgumentException("Type is required");
        }
        try {
            constructor = type.getConstructor(String.class);
            if (!Modifier.isPublic(constructor.getModifiers())) {
                throw new IllegalArgumentException(type.getName() + " does not have a public string-constructor");
            }
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException(type.getName() + " must have a string constructor");
        }
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return super.deserializeWithType(p, ctxt, typeDeserializer);
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        String string = p.getValueAsString();
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        try {
            return constructor.newInstance(string);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IOException("Failed to deserialize value '" + string + "' to " + constructor.getDeclaringClass().getName() + ": " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}