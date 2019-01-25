package ch.frostnova.test.jackson.test.util.converter;

import ch.frostnova.test.jackson.test.util.domain.Metadata;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.Optional;

/**
 * JSON converter for {@link ch.frostnova.test.jackson.test.util.domain.Metadata}
 *
 * @author pwalser
 * @since 25.01.2019
 */

public final class MetadataConverter {

    public static class Serializer extends JsonSerializer<Metadata> {

        @Override
        public void serialize(Metadata metadata, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (metadata != null) {
                jsonGenerator.writeStartObject();
                for (String key : metadata.getKeys()) {
                    jsonGenerator.writeFieldName(key);
                    Optional<String> value = metadata.get(key);
                    if (value.isPresent()) {
                        jsonGenerator.writeObject(value.get());
                    } else {
                        jsonGenerator.writeNull();
                    }
                }
                jsonGenerator.writeEndObject();
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<Metadata> {

        @Override
        public Metadata deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            Metadata metadata = new Metadata();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            node.fieldNames().forEachRemaining(field -> {
                JsonNode value = node.get(field);
                metadata.set(field, Optional.ofNullable(value).map(JsonNode::asText).orElse(null));
            });
            return metadata;
        }
    }
}