package me.jiangcai.wx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jiangcai.lib.test.SpringWebTest;
import me.jiangcai.wx.couple.debug.DebugFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = {WeixinSpringConfig.class, SingleAccountSpringConfig.class})
public abstract class SingleAccountTest extends SpringWebTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    protected DefaultMockMvcBuilder buildMockMVC(DefaultMockMvcBuilder builder) {
        DebugFilter debugFilter = new DebugFilter();
        try {
            debugFilter.init(new MockFilterConfig(servletContext));
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return builder.addFilters(debugFilter);
    }


    protected RequestBuilder postWeixinMessage(String uri, String jsonLocation) throws IOException {
        try (InputStream inputStream = applicationContext.getResource(jsonLocation).getInputStream()) {
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            JsonNode request = jsonNode.get("request");

            JsonNode parameters = request.get("parameters");
            StringBuilder uriBuilder = new StringBuilder(uri);
            if (parameters != null && parameters.isArray()) {
                for (JsonNode parameter : parameters) {
                    String name = parameter.get("name").asText();
                    if (uriBuilder.toString().contains("?")) {
                        uriBuilder.append("&");
                    } else {
                        uriBuilder.append("?");
                    }
                    uriBuilder.append(name).append("=").append(parameter.get("value").asText());
                }
            }

            MockHttpServletRequestBuilder builder = post(uriBuilder.toString());

            JsonNode headers = request.get("headers");
            if (headers != null && headers.isArray()) {
                for (JsonNode header : headers) {
                    String name = header.get("name").asText();
                    if (name.equalsIgnoreCase("host"))
                        continue;
                    if (name.equalsIgnoreCase("content-length"))
                        continue;
                    if (name.equalsIgnoreCase("content-type")) {
                        builder = builder.contentType(MediaType.parseMediaType(header.get("value").asText()));
                        continue;
                    }

                    builder = builder.header(name, header.get("value").asText());
                }
            }

            JsonNode content = request.get("content");
            if (content != null && content.isTextual()) {
                builder = builder.content(content.asText());
            }

            return builder;
        }

    }


}
