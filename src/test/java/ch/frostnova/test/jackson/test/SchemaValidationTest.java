package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.SerialFormat;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SchemaValidationTest {

    private JsonSchema getSchema() throws IOException, ProcessingException {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        URL schemaURL = getClass().getResource("/Movie.schema.json");
        JsonNode schemaNode = SerialFormat.json().parseTree(schemaURL);
        return factory.getJsonSchema(schemaNode);
    }

    @Test
    public void testSchemaValidation() throws Exception {

        Movie movie = Movie.create();
        String json = SerialFormat.json().stringify(movie);
        JsonNode root = SerialFormat.json().parseTree(json);

        ProcessingReport report = getSchema().validate(root, true);
        assertTrue(report.isSuccess());
    }

    @Test
    public void testSchemaValidationValid() throws Exception {
        URL resource = getClass().getResource("/test-data/movie-valid.json");
        JsonNode root = SerialFormat.json().parseTree(resource);

        ProcessingReport report = getSchema().validate(root, true);
        assertTrue(report.isSuccess());
    }

    @Test
    public void testSchemaValidationInvalid() throws Exception {
        URL resource = getClass().getResource("/test-data/movie-invalid.json");
        JsonNode root = SerialFormat.json().parseTree(resource);

        ProcessingReport report = getSchema().validate(root, true);
        Set<String> validationMessages = new HashSet<>();
        for (ProcessingMessage processingMessage : report) {
            validationMessages.add(processingMessage.getMessage());
        }
        assertFalse(report.isSuccess());

        assertTrue(validationMessages.contains("object instance has properties which are not allowed by the schema: [\"nobodyExpects\"]"));
        assertTrue(validationMessages.contains("object has missing required properties ([\"title\"])"));
        assertTrue(validationMessages.contains("object has missing required properties ([\"lastName\"])"));
        assertTrue(validationMessages.contains("numeric instance is lower than the required minimum (minimum: 0, found: -77)"));
        assertTrue(validationMessages.contains("ECMA 262 regex \"\\d{4}-(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|[1-2][0-9]|3[0-1])\" does not match input string \"1942-42-13\""));
        assertTrue(validationMessages.contains("ECMA 262 regex \"^\\d+(\\.\\d+)?\\:\\d+(\\.\\d+)?$\" does not match input string \"555\""));
        assertTrue(validationMessages.contains("string \"yesterday\" is invalid against requested date format(s) [yyyy-MM-dd'T'HH:mm:ssZ, yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,12}Z]"));
        assertTrue(validationMessages.contains("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])"));
        assertTrue(validationMessages.contains("instance type (boolean) does not match any allowed primitive type (allowed: [\"string\"])"));
        assertTrue(validationMessages.contains("numeric instance is lower than the required minimum (minimum: 0, found: -8.2)"));
        assertTrue(validationMessages.contains("numeric instance is lower than the required minimum (minimum: 0, found: -3)"));

        assertEquals(11, validationMessages.size());
    }
}
