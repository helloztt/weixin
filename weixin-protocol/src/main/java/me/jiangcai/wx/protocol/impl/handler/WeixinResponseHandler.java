package me.jiangcai.wx.protocol.impl.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import me.jiangcai.wx.protocol.exception.BadAccessException;
import me.jiangcai.wx.protocol.exception.BadAuthAccessException;
import me.jiangcai.wx.protocol.exception.IllegalOpenIdException;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.response.BaseResponse;
import me.jiangcai.wx.protocol.impl.response.ErrorResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;

/**
 * @author CJ
 */
public class WeixinResponseHandler<T> extends AbstractResponseHandler<T> {

    private static final Log log = LogFactory.getLog(WeixinResponseHandler.class);

    protected final ObjectMapper objectMapper = new ObjectMapper();

    private Class<T> clazz;

    public WeixinResponseHandler() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        try {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
            this.clazz = clazz;
        } catch (ClassCastException ex) {
        }

    }

    public WeixinResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(entity.getContent(), buffer);

        if (log.isDebugEnabled()) {
            log.debug("[WEIXIN] response:" + StreamUtils.copyToString(new ByteArrayInputStream(buffer.toByteArray()), Charset.forName("UTF-8")));
        }

        try {
            return defaultResult(buffer);
        } catch (UnrecognizedPropertyException exception) {
            log.debug("[WEIXIN] unexpected:", exception);
            // 那应该是失败了哦
            return throwException(buffer);
        }
    }

    protected T defaultResult(ByteArrayOutputStream buffer) throws IOException {
        T result = objectMapper.readValue(buffer.toByteArray(), clazz);
        if (result instanceof BaseResponse) {
            if (((BaseResponse) result).getCode() != 0) {
                return throwException(buffer);
            }
        }
        return result;
    }

    private T throwException(ByteArrayOutputStream buffer) throws IOException {
        ErrorResponse response = objectMapper.readValue(buffer.toByteArray(), ErrorResponse.class);

        if (response.getCode() == 0)
            return null;

        log.debug("[WEIXIN] unexpected ErrorResponse:" + response);

        // http://mp.weixin.qq.com/wiki/10/6380dc743053a91c544ffd2b7c959166.html
        if (response.getCode() == 40001 || response.getCode() == 40002)
            throw new BadAccessException();
        //        40029	不合法的oauth_code
        //        40030	不合法的refresh_token
        if (response.getCode() == 40029 || response.getCode() == 40030)
            throw new BadAuthAccessException();

        //
        if (response.getCode() == 40003)
            throw new IllegalOpenIdException();


        // 分析下错误 现在暴力点 直接。。
        throw new ProtocolException(response.getCode(), response.getMessage());
    }
}
