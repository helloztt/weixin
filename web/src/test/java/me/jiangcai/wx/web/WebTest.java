package me.jiangcai.wx.web;

import com.gargoylesoftware.htmlunit.WebClient;
import me.jiangcai.wx.web.thymeleaf.WeixinDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * @author CJ
 */
@ContextConfiguration(classes = WebTest.Config.class)
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

    @Configuration
    @EnableWebMvc
    @Import(Config.ThymeleafConfig.class)
    static class Config extends WebMvcConfigurerAdapter {

        @Autowired
        private ThymeleafViewResolver thymeleafViewResolver;

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            super.addViewControllers(registry);
            registry.addViewController("/js")
                    .setViewName("js.html");
        }

        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            super.configureViewResolvers(registry);
            registry.viewResolver(thymeleafViewResolver);
        }

        @Import(ThymeleafConfig.ThymeleafTemplateConfig.class)
        static class ThymeleafConfig {
            @Autowired
            private TemplateEngine engine;

            @Bean
            private ThymeleafViewResolver thymeleafViewResolver() {
                ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
                viewResolver.setTemplateEngine(engine);
                viewResolver.setCharacterEncoding("UTF-8");
                viewResolver.setContentType("text/html;charset=UTF-8");
                return viewResolver;
            }

            static class ThymeleafTemplateConfig {
                @Autowired
                private WebApplicationContext webApplicationContext;
                @Autowired
                private WeixinDialect weixinDialect;

                @Bean
                public TemplateEngine templateEngine() {
                    SpringTemplateEngine engine = new SpringTemplateEngine();
                    engine.setEnableSpringELCompiler(true);
                    engine.setTemplateResolver(templateResolver());
                    engine.addDialect(weixinDialect);
                    return engine;
                }

                private ITemplateResolver templateResolver() {
                    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
                    resolver.setApplicationContext(webApplicationContext);
                    resolver.setCharacterEncoding("UTF-8");
                    resolver.setPrefix("classpath:/pages/");
                    resolver.setTemplateMode(TemplateMode.HTML);
                    return resolver;
                }
            }

        }
    }

}
