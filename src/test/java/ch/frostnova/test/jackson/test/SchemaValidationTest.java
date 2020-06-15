package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.SerialFormat;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemaValidationTest {

    private Schema getSchema() {
        JSONObject jsonSchema = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/Movie.schema.json")));

        SchemaLoader loader = SchemaLoader.builder()
                .schemaJson(jsonSchema)
                .build();
        return loader.load().build();
    }

    @Test
    public void testSchemaValidation() {

        Movie movie = Movie.create();
        String json = SerialFormat.json().stringify(movie);
        JSONObject root = new JSONObject(new JSONTokener(json));

        getSchema().validate(root);
    }

    @Test
    public void testSchemaValidationValid() {
        JSONObject root = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/test-data/movie-valid.json")));
        getSchema().validate(root);
    }

    @Test
    public void testSchemaValidationInvalid() {
        JSONObject root = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/test-data/movie-invalid.json")));

        try {
            getSchema().validate(root);
            Assert.fail("Expected ValidationException");
        } catch (ValidationException ex) {
            ex.getAllMessages().forEach(m -> System.out.println(m));

            Set<String> validationMessages = ex.getAllMessages().stream().map(String::valueOf).collect(Collectors.toSet());
            assertTrue(validationMessages.contains("#/metadata/something: expected type: String, found: Boolean"));
            assertTrue(validationMessages.contains("#/aspect-ratio: string [555] does not match pattern ^\\d+(\\.\\d+)?\\:\\d+(\\.\\d+)?$"));
            assertTrue(validationMessages.contains("#/year: expected type: Integer, found: Double"));
            assertTrue(validationMessages.contains("#/genres/0: expected type: String, found: Integer"));
            assertTrue(validationMessages.contains("#/created: [yesterday] is not a valid date-time. Expected [yyyy-MM-dd'T'HH:mm:ssZ, yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,9}Z]"));
            assertTrue(validationMessages.contains("#/ratings/IMDB: -8.2 is not higher or equal to 0"));
            assertTrue(validationMessages.contains("#: required key [title] not found"));
            assertTrue(validationMessages.contains("#: extraneous key [nobodyExpects] is not permitted"));
            assertTrue(validationMessages.contains("#/actors/0/firstName: string [?] does not match pattern ^\\p{Lu}\\p{Ll}+$"));
            assertTrue(validationMessages.contains("#/actors/0/dateOfBirth: string [1942-42-13] does not match pattern \\d{4}-(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|[1-2][0-9]|3[0-1])"));
            assertTrue(validationMessages.contains("#/actors/0/age: -77.0 is not higher or equal to 0"));
            assertTrue(validationMessages.contains("#/actors/0: required key [lastName] not found"));
            assertTrue(validationMessages.contains("#/actors/1/lastName: string [dREBIN] does not match pattern ^\\p{Lu}\\p{Ll}+$"));
            assertEquals(validationMessages.size(), 13);
        }
    }
}
