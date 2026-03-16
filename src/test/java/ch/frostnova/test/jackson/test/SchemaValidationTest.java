package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.ObjectMappers;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import org.assertj.core.api.Assertions;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JSON Schema validation test
 *
 * @author pwalser
 * @since 2020-06-15
 */
class SchemaValidationTest {

    private Schema getSchema() {
        var jsonSchema = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/Movie.schema.json")));

        var loader = SchemaLoader.builder()
                .schemaJson(jsonSchema)
                .build();
        return loader.load().build();
    }

    @Test
    void testSchemaValidation() throws IOException {

        var movie = Movie.create();
        var json = ObjectMappers.json().writeValueAsString(movie);
        var root = new JSONObject(new JSONTokener(json));

        getSchema().validate(root);
    }

    @Test
    void testSchemaValidationValid() {
        var root = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/test-data/movie-valid.json")));
        getSchema().validate(root);
    }

    @Test
    void testSchemaValidationInvalid() {
        var root = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/test-data/movie-invalid.json")));

        try {
            getSchema().validate(root);
            Assertions.fail("Expected ValidationException");
        } catch (ValidationException ex) {
            ex.getAllMessages().forEach(System.out::println);

            var validationMessages = ex.getAllMessages().stream().map(String::valueOf).collect(Collectors.toSet());
            validationMessages.stream().sorted().forEach(System.out::println);
            assertThat(validationMessages).containsExactlyInAnyOrder(
                    "#/actors/0/age: -77.0 is not higher or equal to 0",
                    "#/actors/0/dateOfBirth: string [1942-42-13] does not match pattern \\d{4}-(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|[1-2][0-9]|3[0-1])",
                    "#/actors/0/firstName: string [?] does not match pattern ^\\p{Lu}\\p{Ll}+$",
                    "#/actors/0: required key [lastName] not found",
                    "#/actors/1/lastName: string [dREBIN] does not match pattern ^\\p{Lu}\\p{Ll}+$",
                    "#/aspect-ratio: string [555] does not match pattern ^\\d+(\\.\\d+)?\\:\\d+(\\.\\d+)?$",
                    "#/created: [yesterday] is not a valid date-time. Expected [yyyy-MM-dd'T'HH:mm:ssZ, yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,9}Z]",
                    "#/duration: string [123] does not match pattern ^\\s*(?:(?:\\d+)\\s*w)?\\s*(?:(?:\\d+)\\s*d)?\\s*(?:(?:\\d+)\\s*h)?\\s*(?:(?:\\d+)\\s*m)?\\s*(?:(?:\\d+)\\s*s)?\\s*$",
                    "#/genres/0: expected type: String, found: Integer",
                    "#/metadata/something: expected type: String, found: Boolean",
                    "#/ratings/IMDB: -8.2 is not higher or equal to 0",
                    "#/year: expected type: Integer, found: Double",
                    "#: extraneous key [nobodyExpects] is not permitted",
                    "#: required key [title] not found"
            );
        }
    }
}
