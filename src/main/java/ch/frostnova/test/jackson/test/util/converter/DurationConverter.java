package ch.frostnova.test.jackson.test.util.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * JSON converter for {@link Duration}
 *
 * @author pwalser
 * @since 18.05.2021
 */

public final class DurationConverter {

    private final static BinaryOperator<String> TOKEN_PATTERN = (name, unit) -> String.format("(?:(?<%s>\\d+)\\s*%s)?", name, unit);

    private final static Pattern PATTERN = Pattern.compile("^\\s*" + join("\\s*",
            TOKEN_PATTERN.apply("weeks", "w"),
            TOKEN_PATTERN.apply("days", "d"),
            TOKEN_PATTERN.apply("hours", "h"),
            TOKEN_PATTERN.apply("minutes", "m"),
            TOKEN_PATTERN.apply("seconds", "s")
    ) + "\\s*$");

    private static Duration parse(String value) {
        if (value == null || value.trim().length() == 0) {
            return Duration.ofNanos(0);
        }
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Illegal duration format '%s', expected '1w2d3h4m5s' or '1w 2d 3h 4m 5s' format (w=weeks,d=days,h=hours,m=minutes,s=seconds)", value));
        }
        int weeks = Optional.ofNullable(matcher.group("weeks")).map(Integer::parseInt).orElse(0);
        int days = Optional.ofNullable(matcher.group("days")).map(Integer::parseInt).orElse(0);
        int hours = Optional.ofNullable(matcher.group("hours")).map(Integer::parseInt).orElse(0);
        int minutes = Optional.ofNullable(matcher.group("minutes")).map(Integer::parseInt).orElse(0);
        int seconds = Optional.ofNullable(matcher.group("seconds")).map(Integer::parseInt).orElse(0);
        return Duration.ofDays(7 * weeks + days).plus(Duration.ofHours(hours)).plus(Duration.ofMinutes(minutes)).plus(Duration.ofSeconds(seconds));
    }

    private static String format(Duration duration) {
        if (duration == null) {
            return "0s";
        }
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        days %= 7;
        List<String> tokens = new LinkedList<>();
        if (weeks > 0) {
            tokens.add(weeks + "w");
        }
        if (days > 0) {
            tokens.add(days + "d");
        }
        if (hours > 0) {
            tokens.add(hours + "h");
        }
        if (minutes > 0) {
            tokens.add(minutes + "m");
        }
        if (seconds > 0 || tokens.isEmpty()) {
            tokens.add(seconds + "s");
        }
        return tokens.stream().collect(joining(" "));
    }

    public static class Serializer extends JsonSerializer<Duration> {

        @Override
        public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (duration == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeString(format(duration));
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<Duration> {

        @Override
        public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String string = jsonParser.getValueAsString();
            if (string == null || string.trim().length() == 0) {
                return null;
            }
            return parse(string);
        }
    }
}