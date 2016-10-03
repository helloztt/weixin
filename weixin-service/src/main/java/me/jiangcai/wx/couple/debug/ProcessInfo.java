package me.jiangcai.wx.couple.debug;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.jiangcai.wx.couple.debug.format.HandlerSerializer;
import me.jiangcai.wx.couple.debug.format.HttpServletRequestSerializer;
import me.jiangcai.wx.couple.debug.format.HttpServletResponseSerializer;
import me.jiangcai.wx.couple.debug.format.ModelAndViewSerializer;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理信息
 *
 * @author CJ
 */
@Data
public class ProcessInfo {
    @JsonSerialize(using = HttpServletRequestSerializer.class)
    private HttpServletRequest request;
    @JsonSerialize(using = HandlerSerializer.class)
    private Object handler;
    // Exception
    @JsonSerialize(using = ModelAndViewSerializer.class)
    private ModelAndView modelAndView;
    // FlashMap
    @JsonSerialize(using = HttpServletResponseSerializer.class)
    private HttpServletResponse response;
}
