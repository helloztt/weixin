package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.web.flow.RedirectException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CJ
 */
@ControllerAdvice
public class WebAdvice {

    private static final Log log = LogFactory.getLog(WebAdvice.class);

    @ExceptionHandler(RedirectException.class)
    public void forRedirectException(RedirectException exception, HttpServletResponse response) throws IOException {
        log.debug(exception.toString() + " happen.");
        response.sendRedirect(exception.getUrl());
    }

}
