package me.jiangcai.wx.web.thymeleaf;

import me.jiangcai.wx.couple.WeixinRequestHandlerMapping;
import me.jiangcai.wx.model.PublicAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.StandardReplaceTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 它可以帮助配置当前公众号或者指定公众号的JS配置
 * 本身的过程会比较复杂,因为它会自己调度一次ajax请求以让服务端给出参数配置微信sdk
 * <p>
 * 客户端要求预先载入jQuery 版本无要求
 *
 * @author CJ
 * @see StandardReplaceTagProcessor
 */
@Component
public class JsProcessor extends AbstractAttributeTagProcessor implements WeixinProcessor {

    private static final Log log = LogFactory.getLog(JsProcessor.class);
    private static final String[] FixedAPI = new String[]{
            "onMenuShareTimeline",
            "onMenuShareAppMessage",
            "onMenuShareQQ",
            "onMenuShareWeibo",
            "onMenuShareQZone",
            "hideMenuItems",
            "showAllNonBaseMenuItem"
    };
    @Autowired
    private WeixinRequestHandlerMapping mapping;

    //    public JsProcessor() {
//        super(TemplateMode.HTML, "wx",
//                "js", true
//                , "js", true
//                , 100);
//    }
    @Autowired
    private Environment environment;


//    @Override
//    protected void doProcess(ITemplateContext context, IProcessableElementTag tag
//            , IElementTagStructureHandler structureHandler) {
//        log.debug("start!");
//    }

    public JsProcessor() {
        super(TemplateMode.HTML, "wx",
                null, true
                , "js", true
                , 100, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {

        if (!WeixinDialect.Support(context)) {
            structureHandler.removeElement();
            return;
        }

        PublicAccount publicAccount = mapping.currentPublicAccount();
        assert publicAccount != null;

        String[] namedApi = attributeValue.split(",");
        if (namedApi.length == 1 && StringUtils.isEmpty(namedApi[0])) {
            namedApi = new String[0];
        }
        String[] api = new String[namedApi.length + FixedAPI.length];

        System.arraycopy(FixedAPI, 0, api, 0, FixedAPI.length);
        System.arraycopy(namedApi, 0, api, FixedAPI.length, namedApi.length);

        // 把字符串列表改成json array格式
        String apiList;

        StringBuilder apiBuilder = new StringBuilder();
        apiBuilder.append("[");
        for (int i = 0; i < api.length; i++) {
            if (i > 0)
                apiBuilder.append(",");
            apiBuilder.append("'").append(api[i].trim()).append("'");
        }
        apiBuilder.append("]");
        apiList = apiBuilder.toString();

        structureHandler.removeBody();

        // async false!
        StringBuilder builder = new StringBuilder();
        builder.append("<script src=\"//res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>");

        if (environment.acceptsProfiles("test"))
            builder.append("<script>$.ajaxSetup({async:false});</script>");

        // 计算URI
        WebEngineContext webEngineContext = (WebEngineContext) context;

        builder.append("<script>");
        String configUri = webEngineContext.getRequest().getContextPath() + "/weixin/sdk/config";
        try {
            try (InputStream inputStream = new ClassPathResource("/weixin.sdk.loader.js").getInputStream()) {
                String code = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
                code = code.replaceAll("CONFIG_URI", configUri);
                code = code.replaceAll("API_LIST", apiList);
                builder.append(code);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        builder.append("</script>");

        structureHandler.replaceWith(builder.toString(), false);

    }
}
