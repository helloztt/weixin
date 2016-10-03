package me.jiangcai.wx.couple.debug.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import me.jiangcai.wx.couple.debug.NameAndValue;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class HttpServletResponseSerializer extends JsonSerializer<HttpServletResponse> {

    @Override
    public void serialize(HttpServletResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeNull();
        } else {
            Info info = new Info();
            info.setStatus(value.getStatus());
//            info.setError(value.me);
            info.setContentType(value.getContentType());
            ArrayList<NameAndValue> arrayList = new ArrayList<>();
            for (String name : value.getHeaderNames()) {
                value.getHeaders(name)
                        .forEach(str -> arrayList.add(new NameAndValue(name, str)));
            }

            info.setHeaders(arrayList);

            gen.writeObject(info);
        }
    }

    @Data
    private class Info {
        private int status;
        //        private String error;
        private List<NameAndValue> headers;
        private String contentType;
    }
}
