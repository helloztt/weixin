package me.jiangcai.wx.protocol.converter;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import me.jiangcai.wx.message.Message;
import me.jiangcai.wx.message.MessageType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * @author CJ
 */
public class MessageConverter extends AbstractHttpMessageConverter<Message> {

    private static final Log log = LogFactory.getLog(MessageConverter.class);

    private final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(new WstxInputFactory(), new WstxOutputFactory()));

    public MessageConverter() {
        super(MediaType.TEXT_XML);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // clazz 是Message的子类即可
        return Message.class.isAssignableFrom(clazz);
    }

    @Override
    protected Message readInternal(Class<? extends Message> clazz, HttpInputMessage inputMessage) throws IOException
            , HttpMessageNotReadableException {
        try (InputStream inputStream = inputMessage.getBody()) {
            JsonNode tree = xmlMapper.readTree(inputStream);
            String jsonType = tree.get("MsgType").asText();

            MessageType theType = null;
            for (MessageType type : MessageType.values()) {
                if (type.name().equalsIgnoreCase(jsonType)) {
                    theType = type;
                    break;
                }
            }

            if (theType == null) {
                log.warn("unknown MsgType:" + jsonType);
                return null;
            }

            // TODO 不同类型的消息,事件
            return xmlMapper.readValue(xmlMapper.treeAsTokens(tree),theType.messageClass());
        }
    }

    @Override
    protected void writeInternal(Message message, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), "UTF-8")) {
            xmlMapper.writeValue(writer, message);
        }

    }

}
