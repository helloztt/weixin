package me.jiangcai.wxtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CJ
 */
@Controller
public class TestController {

    @RequestMapping(value = "/haha")
    @ResponseBody
    public String haha(){
        return "haha";
    }

}
