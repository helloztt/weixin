package me.jiangcai.wx.standard.entity.support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

/**
 * 用于处理保存Locale的转换器
 *
 * @author CJ
 */
@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {

    @Override
    public String convertToDatabaseColumn(Locale attribute) {
        if (attribute == null)
            return null;
        return attribute.toLanguageTag();
    }

    @Override
    public Locale convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.length() == 0)
            return null;
        return Locale.forLanguageTag(dbData);
    }
}
