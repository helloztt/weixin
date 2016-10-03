package me.jiangcai.wx.couple.debug.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;

/**
 * @author CJ
 */
public class HandlerSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeNull();
        } else {
            if (value instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) value;
                gen.writeObject(new Info(handlerMethod.getBeanType().getName(), handlerMethod.toString()));
            } else {
                gen.writeString(value.getClass().getName());
            }
        }
    }

    @Data
    @AllArgsConstructor
    private class Info {
        private String type;
        private String method;
    }
}
