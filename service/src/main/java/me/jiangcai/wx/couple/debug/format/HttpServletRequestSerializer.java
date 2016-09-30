package me.jiangcai.wx.couple.debug.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import me.jiangcai.wx.couple.debug.NameAndValue;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author CJ
 */
public class HttpServletRequestSerializer extends JsonSerializer<HttpServletRequest> {

    @Data
    private class Info {
        private String method;
        private String uri;
        private String url;
        private String content;
        private List<NameAndValue> parameters;
        private List<NameAndValue> headers;
    }

    @Override
    public void serialize(HttpServletRequest value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeNull();
        } else {
            Info info = new Info();
            info.setMethod(value.getMethod());
            info.setUri(value.getRequestURI());
            info.setUrl(value.getRequestURL().toString());

//            System.out.println();
//            StreamUtils.copy(value.getInputStream(), System.out);
            info.setContent(StreamUtils.copyToString(value.getInputStream(), Charset.forName("UTF-8")));

            ArrayList<NameAndValue> arrayList = new ArrayList<>();
            Enumeration<String> names = value.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Stream.of(value.getParameterValues(name))
                        .forEach(str -> arrayList.add(new NameAndValue(name, str)));
            }
            info.setParameters(arrayList);

            ArrayList<NameAndValue> headers = new ArrayList<>();
            names = value.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Enumeration<String> headerValues = value.getHeaders(name);
                while (headerValues.hasMoreElements()) {
                    headers.add(new NameAndValue(name, headerValues.nextElement()));
                }
            }
            info.setHeaders(headers);

//            RequestContextUtils.getOutputFlashMap(value);
//            RequestContextUtils.getInputFlashMap(value);
//            RequestContext context;
//            @RequestBody arrayList;

            gen.writeObject(info);
        }
    }
}
