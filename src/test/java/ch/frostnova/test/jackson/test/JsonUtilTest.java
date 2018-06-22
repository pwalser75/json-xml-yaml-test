package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Test for {@link JsonUtil}
 *
 * @author pwalser
 * @since 25.01.2018
 */
public class JsonUtilTest {

    @Test
    public void testStringify() {
        Assert.assertEquals(null, JsonUtil.stringify(null));
        Assert.assertEquals("123", JsonUtil.stringify(123));
        Assert.assertEquals("\"456\"", JsonUtil.stringify("456"));
        Assert.assertEquals("true", JsonUtil.stringify(true));
        Assert.assertEquals("\"\"", JsonUtil.stringify(""));

        // arrays
        Assert.assertEquals("[1,2,3,4,5]", JsonUtil.stringify(Arrays.asList(1, 2, 3, 4, 5)).replaceAll("\\s", ""));

        // maps
        Map<String, Object> map = new HashMap<>();
        map.put("a", true);
        map.put("b", 123);
        map.put("c", "456");
        map.put("d", true);
        map.put("e", Arrays.asList(1, 2, 3));
        Assert.assertEquals("{\"a\":true,\"b\":123,\"c\":\"456\",\"d\":true,\"e\":[1,2,3]}", JsonUtil.stringify(map).replaceAll("\\s", ""));
    }

    @Test
    public void testParse() {
        Assert.assertEquals(null, JsonUtil.parse(Object.class, (String) null));
        Assert.assertEquals(Integer.valueOf(123), JsonUtil.parse(Integer.class, "123"));
        Assert.assertEquals("456", JsonUtil.parse(String.class, "\"456\""));
        Assert.assertEquals(Boolean.TRUE, JsonUtil.parse(Boolean.class, "true"));
        Assert.assertEquals("", JsonUtil.parse(String.class, "\"\""));

        // arrays
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5), JsonUtil.parse(LinkedList.class, "[1,2,3,4,5]"));

        // maps
        HashMap<?, ?> map = JsonUtil.parse(HashMap.class, "{\"a\":true,\"b\":123,\"c\":\"456\",\"d\":true,\"e\":[1,2,3]}");
        Assert.assertEquals(true, map.get("a"));
        Assert.assertEquals(123, map.get("b"));
        Assert.assertEquals("456", map.get("c"));
        Assert.assertEquals(Boolean.TRUE, map.get("d"));
        Assert.assertEquals(Arrays.asList(1, 2, 3), map.get("e"));
    }
}
