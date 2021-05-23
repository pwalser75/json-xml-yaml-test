package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.domain.AspectRatio;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

/**
 * Test for {@link ch.frostnova.test.jackson.test.util.domain.AspectRatio}
 *
 * @author pwalser
 * @since 06.07.2018
 */
public class AspectRatioTest {

    private final static Offset EPSILON = offset(1e-10);

    @Test
    void testSimple() {
        AspectRatio aspectRatio = new AspectRatio("16:9");
        assertThat(aspectRatio.getWidth()).isCloseTo(16, EPSILON);
        assertThat(aspectRatio.getHeight()).isCloseTo(9, EPSILON);
        assertThat(aspectRatio.getAspect()).isCloseTo(16d / 9, EPSILON);
        assertThat(aspectRatio.toString()).isEqualTo("16:9");
    }

    @Test
    void testNormalize() {
        AspectRatio aspectRatio = new AspectRatio(16, 9);
        AspectRatio normalized = aspectRatio.normalized();

        assertThat(aspectRatio).isEqualTo(normalized);
        assertThat(normalized.getWidth()).isCloseTo(1.7777777777777777, EPSILON);
        assertThat(normalized.getHeight()).isCloseTo(1, EPSILON);
        assertThat(normalized.getAspect()).isCloseTo(16d / 9, EPSILON);
        assertThat(normalized.toString()).isEqualTo("1.78:1");
    }

    @Test
    void testStringConstruct() {

        List<String> valid = Arrays.asList("16:9", " 16 : \n  9 \t", "123.4 : 9.73 ", "0.16:0.9");
        List<String> invalid = Arrays.asList(null, "", "nope", "16/9", "3.4.5 : 9.8", "16 : nine", "16:0", "-16:-9");

        for (String s : valid) {
            AspectRatio aspectRatio = new AspectRatio(s);
            assertThat(new AspectRatio(aspectRatio.toString())).isEqualTo(aspectRatio);
        }
        for (String s : invalid) {
            assertThatThrownBy(() -> new AspectRatio(s)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testValueConstruct() {
        assertThatThrownBy(() -> new AspectRatio(0, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AspectRatio(0, 5)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AspectRatio(5, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AspectRatio(7, -6)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AspectRatio(-7, 6)).isInstanceOf(IllegalArgumentException.class);
    }

}
