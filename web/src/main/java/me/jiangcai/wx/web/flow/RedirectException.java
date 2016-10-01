package me.jiangcai.wx.web.flow;

import lombok.Getter;

/**
 * 必须跳转到指定的url去。
 *
 * @author CJ
 */
@Getter
public class RedirectException extends RuntimeException {

    private final String url;

    public RedirectException(String url) {
        this.url = url;
    }
}
