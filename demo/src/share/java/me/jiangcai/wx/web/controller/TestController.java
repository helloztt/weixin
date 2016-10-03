package me.jiangcai.wx.web.controller;

import me.jiangcai.wx.OpenId;
import me.jiangcai.wx.model.WeixinUserDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author CJ
 */
@Controller
public class TestController {

    @RequestMapping(method = RequestMethod.GET, value = "/openIdTest")
//    @ResponseBody
    public String openIdTest(@OpenId String id, Model model) {
        model.addAttribute("openId", id);
        return "showOpenId.html";
//        return id;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/userInfo")
    public String userInfo(WeixinUserDetail weixinUserDetail, Model model) {
        model.addAttribute("detail", weixinUserDetail);
        return "userDetail.html";
    }

}
