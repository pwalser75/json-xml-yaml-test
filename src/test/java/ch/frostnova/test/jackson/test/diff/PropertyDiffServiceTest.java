package ch.frostnova.test.jackson.test.diff;

import ch.frostnova.test.jackson.test.util.ObjectMappers;
import ch.frostnova.test.jackson.test.util.diff.PropertyDiffService;
import ch.frostnova.test.jackson.test.util.domain.AspectRatio;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyDiffServiceTest {

    private final ObjectMapper objectMapper = ObjectMappers.json();

    private final PropertyDiffService propertyDiffService = new PropertyDiffService(objectMapper);

    private Movie before;
    private Movie after;

    @BeforeEach
    public void init() {
        before = Movie.create();
        after = clone(before);
    }

    private <T> T clone(T value) {
        if (value == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(objectMapper.writeValueAsBytes(value), value.getClass());
        } catch (Exception e) {
            throw new RuntimeException("could not clone value", e);
        }
    }

    @Test
    public void shouldListPropertyPaths() {

        Map<String, String> propertyPaths = propertyDiffService.listPropertyPaths(before);
        propertyPaths.forEach((propertyPath, value) ->
                System.out.printf("%s = %s\n", propertyPath, value));
    }

    @Test
    public void shouldDetectChanges() {

        before.setSynopsis(null);

        after.setTitle("Changed");
        after.setYear(before.getYear() + 1);
        after.setAspectRatio(new AspectRatio(4, 3));
        after.getActors().remove(after.getActors().size() - 1);
        after.getMetadata().set("new", "value");

        List<PropertyDiffService.PropertyDiff> diff = propertyDiffService.diff(before, after);
        diff.forEach(System.out::println);

        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("aspect-ratio", "2.39:1", "4:3"))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("title", "Blade Runner", "Changed"))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("year", "1982", "1983"))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("metadata.new", null, "value"))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("actors[2].age", "61", null))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("actors[2].dateOfBirth", "1959-11-20", null))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("actors[2].firstName", "Sean", null))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("actors[2].lastName", "Young", null))).isTrue();
        assertThat(diff.contains(new PropertyDiffService.PropertyDiff("synopsis", null, after.getSynopsis()))).isTrue();
    }
}
