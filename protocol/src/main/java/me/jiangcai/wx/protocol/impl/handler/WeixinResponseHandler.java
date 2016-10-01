package me.jiangcai.wx.protocol.impl.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.response.ErrorResponse;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * @author CJ
 */
public class WeixinResponseHandler<T> extends AbstractResponseHandler<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<T> clazz;

    public WeixinResponseHandler() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
        this.clazz = clazz;
    }

    public WeixinResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(entity.getContent(), buffer);

        try {
            return objectMapper.readValue(buffer.toByteArray(), clazz);
        } catch (UnrecognizedPropertyException exception) {
            // 那应该是失败了哦
            ErrorResponse response = objectMapper.readValue(buffer.toByteArray(), ErrorResponse.class);

            if (response.getCode() == 0)
                return null;

            // 分析下错误 现在暴力点 直接。。
            throw new ProtocolException(response.getCode(), response.getMessage());
        }
    }
}
