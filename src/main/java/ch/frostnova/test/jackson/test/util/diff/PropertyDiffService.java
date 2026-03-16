package ch.frostnova.test.jackson.test.util.diff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;

/**
 * Prototype: create a value diff between two DTOs.
 *
 * @author pwalser
 * @since 2021-03-10
 */
public class PropertyDiffService {

    private final ObjectMapper objectMapper;

    public PropertyDiffService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> List<PropertyDiff> diff(T before, T after) {
        requireNonNull(before, "before is required");
        requireNonNull(after, "after is required");

        var beforeValues = listPropertyPaths(before);
        var afterValues = listPropertyPaths(after);

        List<PropertyDiff> result = new ArrayList<>();

        Set<String> commonPropertyPaths = new TreeSet<>(beforeValues.keySet());
        commonPropertyPaths.retainAll(afterValues.keySet());

        Set<String> addedPropertyPaths = new TreeSet<>(afterValues.keySet());
        addedPropertyPaths.removeAll(beforeValues.keySet());

        Set<String> removedPropertyPaths = new TreeSet<>(beforeValues.keySet());
        removedPropertyPaths.removeAll(afterValues.keySet());

        for (var propertyPath : commonPropertyPaths) {
            var beforeValue = beforeValues.get(propertyPath);
            var afterValue = afterValues.get(propertyPath);
            if (!Objects.equals(beforeValue, afterValue)) {
                result.add(new PropertyDiff(propertyPath, beforeValue, afterValue));
            }
        }
        for (var propertyPath : addedPropertyPaths) {
            var afterValue = afterValues.get(propertyPath);
            result.add(new PropertyDiff(propertyPath, null, afterValue));
        }
        for (var propertyPath : removedPropertyPaths) {
            var beforeValue = beforeValues.get(propertyPath);
            result.add(new PropertyDiff(propertyPath, beforeValue, null));
        }
        return result;
    }

    public <T> Map<String, String> listPropertyPaths(T value) {

        try {
            var root = objectMapper.readTree(objectMapper.writeValueAsString(value));
            Map<String, String> result = new TreeMap<>();
            listPropertyPaths("", root, result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("could not determine property paths", e);
        }
    }

    private void listPropertyPaths(String basePath, JsonNode jsonNode, Map<String, String> result) {

        if (jsonNode.isNull()) {
            return;
        }
        if (jsonNode.isBoolean()) {
            result.put(basePath, String.valueOf(jsonNode.booleanValue()));
        } else if (jsonNode.isNumber()) {
            result.put(basePath, String.valueOf(jsonNode.numberValue()));
        } else if (jsonNode.isTextual()) {
            result.put(basePath, jsonNode.textValue());
        } else if (jsonNode.isArray()) {
            for (var i = 0; i < jsonNode.size(); i++) {
                listPropertyPaths(String.format("%s[%d]", basePath, i), jsonNode.get(i), result);
            }
        } else if (jsonNode.isObject()) {
            var fieldNames = jsonNode.fieldNames();
            while (fieldNames.hasNext()) {
                var fieldName = fieldNames.next();
                listPropertyPaths(basePath.isEmpty() ? fieldName : String.format("%s.%s", basePath, fieldName), jsonNode.get(fieldName), result);
            }
        }
    }

    public static class PropertyDiff {

        private final String propertyPath;
        private final String oldValue;
        private final String newValue;

        public PropertyDiff(String propertyPath, String oldValue, String newValue) {
            this.propertyPath = propertyPath;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        public String getOldValue() {
            return oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        @Override
        public String toString() {
            return String.format("'%s' changed from '%s' to '%s'", propertyPath, oldValue, newValue);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            var that = (PropertyDiff) o;
            return Objects.equals(propertyPath, that.propertyPath) && Objects.equals(oldValue, that.oldValue) && Objects.equals(newValue, that.newValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(propertyPath, oldValue, newValue);
        }
    }
}
