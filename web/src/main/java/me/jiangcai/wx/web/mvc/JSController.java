package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author CJ
 */
@Controller
public class JSController {

    private static final Log log = LogFactory.getLog(JSController.class);

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/weixin/sdk/config", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object config(PublicAccount publicAccount, @RequestBody String url) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString().replace("-", "");

        String signature = Protocol.forAccount(publicAccount).javascriptSign(timestamp, nonceStr, url);

        HashMap<String, Object> data = new HashMap<>();

        data.put("debug", environment.acceptsProfiles("test"));
        data.put("appId", publicAccount.getAppID());
        data.put("timestamp", timestamp);
        data.put("nonceStr", nonceStr);
        data.put("signature", signature);
        data.put("jsApiList", new String[0]);

        return data;
    }

}
