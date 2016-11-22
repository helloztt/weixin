package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import me.jiangcai.wx.model.SceneCode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateQRCodeResponse extends BaseResponse {
    private String ticket;
    @JsonProperty("expire_seconds")
    private int time;
    private String url;

    @SneakyThrows(UnsupportedEncodingException.class)
    public SceneCode toCode() {
        SceneCode code = new SceneCode();
        code.setUrl(url);
        code.setImageURL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket, "UTF-8"));
        return code;
    }
}
