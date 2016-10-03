package me.jiangcai.wx.web;

import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author CJ
 */
@ContextConfiguration(classes = SimpleMVCConfig.class)
public abstract class WebTest extends BaseTest {

    protected MockHttpServletRequestBuilder getWeixin(String urlTemplate, Object... urlVariables) {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(urlTemplate, urlVariables);
        builder.header("user-agent", "MicroMessenger");
        return builder;
    }


    @Override
    protected void createWebDriver() {
        driver = MockMvcHtmlUnitDriverBuilder
                .mockMvcSetup(mockMvc)
                .useMockMvcForHosts("wxtest.jiangcai.me")
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

}
