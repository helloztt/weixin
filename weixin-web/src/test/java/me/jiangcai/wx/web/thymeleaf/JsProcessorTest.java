package me.jiangcai.wx.web.thymeleaf;

import com.google.common.base.Predicate;
import me.jiangcai.wx.web.WebTest;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nullable;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author CJ
 */
@ActiveProfiles({"test", "wx.url.test"})
@ContextConfiguration(classes = JsProcessorTest.Config.class)
public class JsProcessorTest extends WebTest {

    @Test
    public void demo() throws Exception {


        mockMvc.perform(getWeixin("/js")
        )
                .andDo(print())
        ;

        driver.get("http://localhost/js");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        webDriverWait.until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(@Nullable WebDriver input) {
                JavascriptExecutor executor = (JavascriptExecutor) input;
                assert executor != null;
                Object value = executor.executeScript("return wx.testConfigDone");
                return value != null && value instanceof Boolean && (Boolean) value;
            }
        });

        System.out.println(driver.getPageSource());

    }

    @Configuration
    @PropertySource("classpath:/wx_test.properties")
    static class Config {

    }

}