package ch.frostnova.test.jackson.test.util.protobuf;

import ch.frostnova.test.jackson.test.proto.MovieProtos;
import ch.frostnova.test.jackson.test.util.domain.Actor;
import ch.frostnova.test.jackson.test.util.domain.AspectRatio;
import ch.frostnova.test.jackson.test.util.domain.Genre;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Maps between the {@link Movie} domain object and the generated {@link MovieProtos.MovieMessage}.
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MovieProtobufMapper {

    MovieProtobufMapper INSTANCE = Mappers.getMapper(MovieProtobufMapper.class);

    @Mapping(target = "created", source = "created", qualifiedByName = "instantToEpochMs")
    @Mapping(target = "aspectRatio", source = "aspectRatio", qualifiedByName = "aspectRatioToString")
    @Mapping(target = "durationSeconds", source = "duration", qualifiedByName = "durationToSeconds")
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    MovieProtos.MovieMessage toProto(Movie movie);

    @AfterMapping
    default void fillProtoCollections(Movie movie, @MappingTarget MovieProtos.MovieMessage.Builder builder) {
        if (movie.getGenres() != null) {
            movie.getGenres().forEach(g -> builder.addGenres(toProtoGenre(g)));
        }
        if (movie.getRatings() != null) {
            movie.getRatings().forEach((k, v) -> builder.putRatings(k, v.doubleValue()));
        }
        if (movie.getActors() != null) {
            movie.getActors().forEach(a -> builder.addActors(toProtoActor(a)));
        }
        if (movie.getMetadata() != null) {
            movie.getMetadata().getKeys().forEach(k ->
                    movie.getMetadata().get(k).ifPresent(v -> builder.putMetadata(k, v)));
        }
    }

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "created", source = "created", qualifiedByName = "epochMsToInstant")
    @Mapping(target = "title", source = "title", qualifiedByName = "emptyToNull")
    @Mapping(target = "synopsis", source = "synopsis", qualifiedByName = "emptyToNull")
    @Mapping(target = "aspectRatio", source = "aspectRatio", qualifiedByName = "parseAspectRatio")
    @Mapping(target = "duration", source = "durationSeconds", qualifiedByName = "secondsToDuration")
    @Mapping(target = "genres", source = "genresList")
    @Mapping(target = "ratings", source = "ratingsMap")
    @Mapping(target = "actors", source = "actorsList")
    @Mapping(target = "metadata", ignore = true)
    Movie fromProto(MovieProtos.MovieMessage proto);

    @AfterMapping
    default void fillMetadata(MovieProtos.MovieMessage proto, @MappingTarget Movie movie) {
        proto.getMetadataMap().forEach((k, v) -> movie.getMetadata().set(k, v));
    }

    default MovieProtos.ActorMessage toProtoActor(Actor actor) {
        var builder = MovieProtos.ActorMessage.newBuilder();
        if (actor.getFirstName() != null) builder.setFirstName(actor.getFirstName());
        if (actor.getLastName() != null) builder.setLastName(actor.getLastName());
        if (actor.getBirthDate() != null) builder.setBirthDate(actor.getBirthDate().toString());
        return builder.build();
    }

    default Actor fromProtoActor(MovieProtos.ActorMessage proto) {
        var birthDate = proto.getBirthDate().isEmpty() ? null : LocalDate.parse(proto.getBirthDate());
        var firstName = proto.getFirstName().isEmpty() ? null : proto.getFirstName();
        var lastName = proto.getLastName().isEmpty() ? null : proto.getLastName();
        return new Actor(firstName, lastName, birthDate);
    }

    default MovieProtos.GenreValue toProtoGenre(Genre genre) {
        return MovieProtos.GenreValue.valueOf(genre.name());
    }

    default Genre fromProtoGenre(MovieProtos.GenreValue value) {
        return Genre.valueOf(value.name());
    }

    @Named("instantToEpochMs")
    default Long instantToEpochMs(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }

    @Named("aspectRatioToString")
    default String aspectRatioToString(AspectRatio ar) {
        return ar.toString();
    }

    @Named("durationToSeconds")
    default long durationToSeconds(Duration d) {
        return d.getSeconds();
    }

    @Named("epochMsToInstant")
    default Instant epochMsToInstant(Long epochMs) {
        return epochMs == null ? null : Instant.ofEpochMilli(epochMs);
    }

    @Named("emptyToNull")
    default String emptyToNull(String s) {
        return s.isEmpty() ? null : s;
    }

    @Named("parseAspectRatio")
    default AspectRatio parseAspectRatio(String s) {
        return s.isEmpty() ? null : new AspectRatio(s);
    }

    @Named("secondsToDuration")
    default Duration secondsToDuration(long seconds) {
        return seconds != 0 ? Duration.ofSeconds(seconds) : null;
    }
}
