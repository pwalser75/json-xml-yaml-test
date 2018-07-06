package ch.frostnova.test.jackson.test.util.converter;

import ch.frostnova.test.jackson.test.util.domain.AspectRatio;

/**
 * Value object deserializer for {@link AspectRatio}.
 *
 * @author pwalser
 * @since 06.07.2018
 */
public class AspectRatioDeserializer extends ValueObjectDeserializer<AspectRatio> {

    public AspectRatioDeserializer() {
        super(AspectRatio.class);
    }
}
