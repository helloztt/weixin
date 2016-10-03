package me.jiangcai.wx.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author CJ
 */
public class ScopeDeserializer extends JsonDeserializer<String[]> {
    @Override
    public String[] deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String text = p.readValueAs(String.class);
        if (text == null || text.trim().length() == 0)
            return null;
        return text.trim().split(",");
    }
}
