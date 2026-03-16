package ch.frostnova.test.jackson.test.util.util;

import java.util.concurrent.Callable;

public class Unchecked {
    private Unchecked() {

    }

    public static <V> V unchecked(Callable<V> code) {
        try {
            return code.call();
        } catch (Exception ex) {
            throw (ex instanceof RuntimeException rte) ? rte : new RuntimeException(ex);
        }
    }
}
