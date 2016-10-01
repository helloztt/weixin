package me.jiangcai.wx.web.test;

import me.jiangcai.wx.OpenId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CJ
 */
@Controller
public class TestController {
    @RequestMapping(method = RequestMethod.GET, value = "/openIdTest")
    @ResponseBody
    public String openIdTest(@OpenId String id) {
        return id;
    }
}
