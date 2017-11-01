package me.jiangcai.wx.protocol.impl.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class MediaItemResponseHandler<T> extends WeixinResponseHandler<Page<T>> {
    private final Class<T> type;
    private final Pageable pageable;

    public MediaItemResponseHandler(Class<T> type, Pageable pageable) {
//        super(Page.class);
        this.type = type;
        this.pageable = pageable;
    }

    @Override
    protected Page<T> defaultResult(ByteArrayOutputStream buffer) throws IOException {
        JsonNode root = objectMapper.readTree(buffer.toByteArray());

        JsonNode items = root.get("item");

        List<T> list = new ArrayList<>();

        for (JsonNode item : items) {
            final JsonParser p = objectMapper.treeAsTokens(item);
            T object = objectMapper.readValue(p, type);
            list.add(object);
        }

        return new PageImpl<>(list, pageable, root.get("total_count").asLong());
    }
}
