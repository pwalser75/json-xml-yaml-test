package ch.frostnova.test.jackson.test.util.domain;

import ch.frostnova.test.jackson.test.util.converter.AspectRatioDeserializer;
import ch.frostnova.test.jackson.test.util.converter.ValueObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value object: an aspect ratio
 *
 * @author pwalser
 * @since 06.07.2018
 */
@JsonSerialize(using = ValueObjectSerializer.class)
@JsonDeserialize(using = AspectRatioDeserializer.class)
public class AspectRatio {

    private final static Pattern PATTERN = Pattern.compile("\\s*(\\d+(?:\\.\\d+)?)\\s*:\\s*(\\d+(?:\\.\\d+)?)\\s*");
    private final static DecimalFormat numberFormat = new DecimalFormat("0.##");

    private double width;
    private double height;

    public AspectRatio(String formatted) {

        if (formatted == null) {
            throw new IllegalArgumentException("Illegal aspect ratio: " + formatted);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
}
