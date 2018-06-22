package ch.frostnova.test.jackson.test.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collection utility functions.
 *
 * @author pwalser
 * @since 18.06.2018
 */
public final class CollectionUtil {

    private CollectionUtil() {

    }

    /**
     * Create a new {@link List} using the given values.
     *
     * @param values values
     * @param <T>    generic type
     * @return list containing the values
     */
    public static <T> List<T> asList(T... values) {
        return Stream.of(values).collect(Collectors.toList());
    }

    /**
     * Create a new Set using the given values.
     *
     * @param values values
     * @param <T>    generic type
     * @return set containing the values
     */
    public static <T> Set<T> asSet(T... values) {
        return Stream.of(values).collect(Collectors.toSet());
    }

    /**
     * Compares two sets for equality (same elements, ignoring the order)
     *
     * @param a   first collection
     * @param b   second collection
     * @param <T> generic type
     * @return equality
     */
    public static <T> boolean equalContent(Collection<? extends T> a, Collection<? extends T> b) {
        if (a == b) {
            return true;
        }
        if (a == null && b != null || a != null && b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        if (!a.containsAll(b) || !b.containsAll(a)) {
            return false;
        }
        return true;
    }

    /**
     * Compares two sets for equality (same elements)
     *
     * @param a   first set
     * @param b   second set
     * @param <T> generic type
     * @return equality
     */
    public static <T> boolean equals(Set<? extends T> a, Set<? extends T> b) {
        return equalContent(a, b);
    }

    /**
     * Compares two lists for equality (same elements in same order)
     *
     * @param a   first list
     * @param b   second list
     * @param <T> generic type
     * @return equality
     */
    public static <T> boolean equals(List<? extends T> a, List<? extends T> b) {

        if (a == b) {
            return true;
        }
        if (a == null && b != null || a != null && b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }

        Iterator<? extends T> ia = a.iterator();
        Iterator<? extends T> ib = b.iterator();
        while (ia.hasNext()) {
            if (!checkEquals(ia.next(), ib.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two maps for equality (same keys with same values)
     *
     * @param a   first map
     * @param b   second map
     * @param <K> generic key type
     * @param <V> generic value type
     * @return equality
     */
    public static <K, V> boolean equals(Map<K, ? extends V> a, Map<K, ? extends V> b) {

        if (a == b) {
            return true;
        }
        if (a == null && b != null || a != null && b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        Set<K> aKeys = a.keySet();
        Set<K> bKeys = b.keySet();
        if (!checkEquals(aKeys, bKeys)) {
            return false;
        }
        for (K key : aKeys) {
            if (!checkEquals(a.get(key), b.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * generic comparison using equals(), with special handling for set, list and map.
     *
     * @param a first object
     * @param b second object
     * @return equality
     */
    private static boolean checkEquals(Object a, Object b) {
        if (a instanceof Set && b instanceof Set) {
            return equals((Set<?>) a, (Set<?>) b);
        }
        if (a instanceof List && b instanceof List) {
            return equals((List<?>) a, (List<?>) b);
        }
        if (a instanceof Map && b instanceof Map) {
            return equals((Map) a, (Map) b);
        }
        return Objects.equals(a, b);
    }

}
