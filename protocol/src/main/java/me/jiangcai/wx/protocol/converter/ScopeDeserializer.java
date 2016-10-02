package me.jiangcai.wx.protocol.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author CJ
 */
public class ScopeDeserializer extends JsonDeserializer<String[]> {
    @Override
    public String[] deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String text = p.readValueAs(String.class);
        if (StringUtils.isEmpty(text))
            return null;
        return text.split(",");
    }
}
