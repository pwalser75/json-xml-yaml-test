package ch.frostnova.test.jackson.test.util.serializer;

import ch.frostnova.test.jackson.test.proto.MovieProtos;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import ch.frostnova.test.jackson.test.util.protobuf.MovieProtobufMapper;

import static ch.frostnova.test.jackson.test.util.util.Unchecked.unchecked;

public class MovieProtobufSerializer implements Serializer<Movie> {

    private final MovieProtobufMapper mapper = MovieProtobufMapper.INSTANCE;

    @Override
    public byte[] serialize(Movie value) {
        var protoMessage = mapper.toProto(value);
        return protoMessage.toByteArray();
    }

    @Override
    public Movie deserialize(byte[] serialized) {
        return unchecked(() -> mapper.fromProto(MovieProtos.MovieMessage.parseFrom(serialized)));
    }
}
