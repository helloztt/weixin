package me.jiangcai.wxtest;

import com.gargoylesoftware.htmlunit.WebClient;
import me.jiangcai.lib.test.SpringWebTest;
import me.jiangcai.wx.couple.debug.DebugFilter;
import me.jiangcai.wxtest.config.MyConfig;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;

import javax.servlet.ServletException;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = MyConfig.class)
public class LoaderTest extends SpringWebTest {

    @Override
    protected DefaultMockMvcBuilder buildMockMVC(DefaultMockMvcBuilder builder) {
        DebugFilter debugFilter = new DebugFilter();
        try {
            debugFilter.init(new MockFilterConfig(servletContext));
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return builder.addFilters(debugFilter);
    }

    @Override
    protected void createWebDriver() {
        driver = MockMvcHtmlUnitDriverBuilder
                .mockMvcSetup(mockMvc)
//                .useMockMvcForHosts("")
                .withDelegate(new WebConnectionHtmlUnitDriver() {
                    @Override
                    protected WebClient modifyWebClientInternal(WebClient webClient) {
                        webClient.addRequestHeader("user-agent", "MicroMessenger");
                        return super.modifyWebClientInternal(webClient);
                    }
                })
                // DIY by interface.
                .build();
    }


    @Test
    public void js() throws Exception {
        driver.get("http://localhost/js.html");
        System.out.println(driver.getPageSource());
    }

}