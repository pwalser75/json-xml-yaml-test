package ch.frostnova.test.jackson.test.util.domain;

import ch.frostnova.test.jackson.test.util.converter.MetadataConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * Metadata, in key/value format
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonSerialize(using = MetadataConverter.Serializer.class)
@JsonDeserialize(using = MetadataConverter.Deserializer.class)
public class Metadata {

    final private Map<String, String> metadata = new TreeMap<>();

    /**
     * Get a metadata value
     *
     * @param key key, required
     * @return optional value
     */
    public Optional<String> get(String key) {
        return Optional.ofNullable(key).map(metadata::get);
    }

    /**
     * Set a metadata value
     *
     * @param key   key, required
     * @param value value, optional (if absent: remove entry)
     */
    public void set(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is required");
        }
        metadata.put(key, value);
    }

    /**
     * Remove a metadata value
     *
     * @param key key, required
     */
    public void remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is required");
        }
        metadata.remove(key);
    }

    /**
     * Return the keys of all entries
     *
     * @return keys
     */
    public Set<String> getKeys() {
        return metadata.keySet();
    }
}
