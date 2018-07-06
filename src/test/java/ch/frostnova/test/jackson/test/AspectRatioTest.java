package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.domain.AspectRatio;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link ch.frostnova.test.jackson.test.util.domain.AspectRatio}
 *
 * @author pwalser
 * @since 06.07.2018
 */
public class AspectRatioTest {

    private final static double EPSILON = 1e-10;

    @Test
    public void testSimple() {
        AspectRatio aspectRatio = new AspectRatio("16:9");
        Assert.assertEquals(16, aspectRatio.getWidth(), EPSILON);
        Assert.assertEquals(9, aspectRatio.getHeight(), EPSILON);
        Assert.assertEquals(16d / 9, aspectRatio.getAspect(), EPSILON);
        Assert.assertEquals("16:9", aspectRatio.toString());
    }

    @Test
    public void testNormalize() {
        AspectRatio aspectRatio = new AspectRatio(16, 9);
        AspectRatio normalized = aspectRatio.normalized();
        Assert.assertEquals(aspectRatio, normalized);
        Assert.assertEquals(16d / 9, normalized.getWidth(), EPSILON);
        Assert.assertEquals(1, normalized.getHeight(), EPSILON);
        Assert.assertEquals("16:9", aspectRatio.toString());
    }

    @Test
    public void testStringConstruct() {

        List<String> valid = Arrays.asList("16:9", " 16 : \n  9 \t", "123.4 : 9.73 ", "0.16:0.9");
        List<String> invalid = Arrays.asList(null, "", "nope", "16/9", "3.4.5 : 9.8", "16 : nine", "16:0", "-16:-9");

        for (String s : valid) {
            AspectRatio aspectRatio = new AspectRatio(s);
            Assert.assertEquals(aspectRatio, new AspectRatio(aspectRatio.toString()));
        }
        for (String s : invalid) {
            try {
                new AspectRatio(s);
                Assert.fail("Expected exception for input string: " + s);
            } catch (IllegalArgumentException expected) {
                // as expected
            }
        }
    }

    @Test
    public void testValueConstruct() {

        try {
            new AspectRatio(0, 0);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException expected) {
            // as expected
        }
        try {
            new AspectRatio(0, 5);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException expected) {
            // as expected
        }
        try {
            new AspectRatio(-7, 6);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException expected) {
            // as expected
        }
        try {
            new AspectRatio(7, -6);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException expected) {
            // as expected
        }
        try {
            new AspectRatio(-7, -6);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException expected) {
            // as expected
        }
    }

}
