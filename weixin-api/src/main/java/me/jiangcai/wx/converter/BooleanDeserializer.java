package me.jiangcai.wx.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * @author CJ
 */
public class BooleanDeserializer extends StdDeserializer<Boolean> {

    public BooleanDeserializer() {
        super(Integer.class);
    }

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext context) throws IOException {
        int data = this._parseIntPrimitive(p, context);
        return data != 0;
    }
}
