package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.SerialFormat;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Test for {@link ch.frostnova.test.jackson.test.util.SerialFormat.JSON}
 *
 * @author pwalser
 * @since 25.01.2018
 */
public class JsonFormatTest {


    @Test
    public void testStringify() {
        Assert.assertEquals(null, SerialFormat.json().stringify(null));
        Assert.assertEquals("123", SerialFormat.json().stringify(123));
        Assert.assertEquals("\"456\"", SerialFormat.json().stringify("456"));
        Assert.assertEquals("true", SerialFormat.json().stringify(true));
        Assert.assertEquals("\"\"", SerialFormat.json().stringify(""));

        // arrays
        Assert.assertEquals("[1,2,3,4,5]", SerialFormat.json().stringify(Arrays.asList(1, 2, 3, 4, 5)).replaceAll("\\s", ""));

        // maps
        Map<String, Object> map = new HashMap<>();
        map.put("a", true);
        map.put("b", 123);
        map.put("c", "456");
        map.put("d", true);
        map.put("e", Arrays.asList(1, 2, 3));
        Assert.assertEquals("{\"a\":true,\"b\":123,\"c\":\"456\",\"d\":true,\"e\":[1,2,3]}", SerialFormat.json().stringify(map).replaceAll("\\s", ""));
    }

    @Test
    public void testParse() {
        Assert.assertEquals(null, SerialFormat.json().parse(Object.class, (String) null));
        Assert.assertEquals(Integer.valueOf(123), SerialFormat.json().parse(Integer.class, "123"));
        Assert.assertEquals("456", SerialFormat.json().parse(String.class, "\"456\""));
        Assert.assertEquals(Boolean.TRUE, SerialFormat.json().parse(Boolean.class, "true"));
        Assert.assertEquals("", SerialFormat.json().parse(String.class, "\"\""));

        // arrays
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5), SerialFormat.json().parse(LinkedList.class, "[1,2,3,4,5]"));

        // maps
        HashMap<?, ?> map = SerialFormat.json().parse(HashMap.class, "{\"a\":true,\"b\":123,\"c\":\"456\",\"d\":true,\"e\":[1,2,3]}");
        Assert.assertEquals(true, map.get("a"));
        Assert.assertEquals(123, map.get("b"));
        Assert.assertEquals("456", map.get("c"));
        Assert.assertEquals(Boolean.TRUE, map.get("d"));
        Assert.assertEquals(Arrays.asList(1, 2, 3), map.get("e"));
    }
}
