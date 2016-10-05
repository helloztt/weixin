package me.jiangcai.wx.web.thymeleaf;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 一个是一些基本信息,还有一个是回调
 * title,desc,link,imgUrl,success,cancel
 *
 * @author CJ
 */
@Component
public class ShareProcessor extends AbstractAttributeTagProcessor implements WeixinProcessor {

    private static String ExpressionString(ITemplateContext context, String input) {
        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
        try {
            return expressionParser.parseExpression(context, input).execute(context).toString();
        } catch (Exception ignored) {
            return input;
        }
    }

    private static WeixinShare parseWeixinShare(ITemplateContext context, IProcessableElementTag tag, String input) {
        Validate.notNull(context, "Configuration cannot be null");
        Validate.notNull(tag, "Processing Context cannot be null");
        Validate.notNull(input, "Input cannot be null");

        String[] data = input.split("\\|");
//        if (data.length != 5)
//            throw new IllegalArgumentException("should be title|desc|link|imgUrl|success|cancel");
        // 总得有默认数据的吧
        String title = "";
        if (data.length > 0)
            title = ExpressionString(context, data[0]);
        String desc = "";
        if (data.length > 1)
            desc = ExpressionString(context, data[1]);
        String link = "";
        if (data.length > 2)
            link = ExpressionString(context, data[2]);
        String imgUrl = "";
        if (data.length > 3)
            imgUrl = ExpressionString(context, data[3]);
        String success = null;
        if (data.length > 4)
            success = ExpressionString(context, data[4]);
        String cancel = null;
        if (data.length > 5)
            cancel = ExpressionString(context, data[5]);

        return new WeixinShare(
                title, desc, link, imgUrl, success, cancel
        );

    }

    @Data
    @AllArgsConstructor
    private static class WeixinShare {
        private String title;
        private String desc;
        private String link;
        private String imgUrl;
        private String success;
        private String cancel;
    }

    protected ShareProcessor() {
        super(TemplateMode.HTML, "wx", null, true, "share", true, 100, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {

        final WeixinShare weixinShare = parseWeixinShare(context, tag, attributeValue);

        try {
            try (InputStream inputStream = new ClassPathResource("/weixin.share.js").getInputStream()) {
                String code = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
                code = code.replaceAll("_TITLE_", weixinShare.title);
                code = code.replaceAll("_DESC_", weixinShare.desc);
                code = code.replaceAll("_LINK_", weixinShare.link);
                code = code.replaceAll("_IMAGE_URL_", weixinShare.imgUrl);
                code = code.replaceAll("_SUCCESS_", weixinShare.success != null ? weixinShare.success : "null");
                code = code.replaceAll("_CANCEL_", weixinShare.cancel != null ? weixinShare.cancel : "null");

                structureHandler.setBody(code, false);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


    }
}
