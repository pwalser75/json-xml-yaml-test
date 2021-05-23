package ch.frostnova.test.jackson.test.util.domain;

import ch.frostnova.test.jackson.test.util.converter.StringConstructorDeserializer;
import ch.frostnova.test.jackson.test.util.converter.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value object: an aspect ratio
 *
 * @author pwalser
 * @since 06.07.2018
 */
@JsonSerialize(using = ToStringSerializer.class)
@JsonDeserialize(using = AspectRatio.AspectRatioDeserializer.class)
public class AspectRatio {

    private final static Pattern PATTERN = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)\\s*:\\s*(\\d+(?:\\.\\d+)?)\\s*");
    private final static DecimalFormat numberFormat = new DecimalFormat("0.##", new DecimalFormatSymbols(Locale.US));
    private final double width;
    private final double height;

    public AspectRatio(String formatted) {

        if (formatted == null) {
            throw new IllegalArgumentException("Aspect ratio is required");
        }
        Matcher matcher = PATTERN.matcher(formatted);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Illegal aspect ratio: " + formatted);
        }
        width = Double.parseDouble(matcher.group(1));
        height = Double.parseDouble(matcher.group(2));
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Illegal aspect ratio: " + width + " : " + height);
        }
    }

    public AspectRatio(double width, double height) {

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Illegal aspect ratio: " + width + " : " + height);
        }
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getAspect() {
        return width / height;
    }

    public AspectRatio normalized() {
        return new AspectRatio(getAspect(), 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AspectRatio other = (AspectRatio) o;
        return Objects.equals(getAspect(), other.getAspect());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getAspect());
    }

    @Override
    public String toString() {
        return numberFormat.format(width) + ":" + numberFormat.format(height);
    }

    static class AspectRatioDeserializer extends StringConstructorDeserializer<AspectRatio> {
        public AspectRatioDeserializer() {
            super(AspectRatio.class);
        }
    }
}
