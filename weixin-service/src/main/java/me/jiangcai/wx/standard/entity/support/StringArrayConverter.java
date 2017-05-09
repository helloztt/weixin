package me.jiangcai.wx.standard.entity.support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author CJ
 */
@Converter
public class StringArrayConverter implements AttributeConverter<String[], String> {

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        return null;
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        return new String[0];
    }
}
