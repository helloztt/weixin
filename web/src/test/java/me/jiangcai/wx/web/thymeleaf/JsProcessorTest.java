package me.jiangcai.wx.web.thymeleaf;

import com.google.common.base.Predicate;
import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.web.WebTest;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author CJ
 */
@ActiveProfiles({"test", "wx.url.test"})
@ContextConfiguration(classes = JsProcessorTest.Config.class)
public class JsProcessorTest extends WebTest {

    @Autowired
    private PublicAccountSupplier supplier;

    //    @Test
    public void code() {
        // 这里是因为我们拿到了现成的code 所以来试验 实际上是不可能获得的
        Protocol protocol = Protocol.forAccount(supplier.findByHost(null));
        String id = protocol.userToken("011ptEbp0SkSMc1Xf08p0adJbp0ptEbx", null);
        assertThat(id)
                .isNotEmpty();
    }

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

    }

    @Configuration
    @PropertySource("classpath:/wx_test.properties")
    static class Config {

    }

}