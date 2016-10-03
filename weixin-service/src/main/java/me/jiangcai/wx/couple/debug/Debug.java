package me.jiangcai.wx.couple.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author CJ
 */
@Component
public class Debug implements HandlerInterceptor {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static boolean work(Environment environment) {
        return environment.acceptsProfiles("test", "debug");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 往用户目录这里丢吧
        // 并且分时段
        // logs/wx-yyyyMMdd/time
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.CHINA);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmmss-SSS", Locale.CHINA);

        Path toFile = Paths.get(".", "logs", "wx-" + dateFormat.format(time), timeFormat.format(time) + ".json");
        Files.createDirectories(toFile.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(toFile, Charset.forName("UTF-8"), StandardOpenOption.CREATE_NEW)) {
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.setRequest(request);
            processInfo.setResponse(response);
            processInfo.setHandler(handler);
            processInfo.setModelAndView(modelAndView);

            objectMapper.writeValue(writer, processInfo);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
