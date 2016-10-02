package me.jiangcai.wx.web.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 必须跳转到指定的url去。
 *
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RedirectException extends RuntimeException {

    private final String url;

    public RedirectException(String url) {
        this.url = url;
    }
}
