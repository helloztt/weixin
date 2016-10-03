package me.jiangcai.wx.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    public LocalDateTimeDeserializer() {
        super(Long.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
        long time = this._parseLong(p, context);
        return new Timestamp(time * 1000).toLocalDateTime();
    }

}
