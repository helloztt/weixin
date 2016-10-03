package me.jiangcai.wx.couple.debug.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CJ
 */
public class ModelAndViewSerializer extends JsonSerializer<ModelAndView> {

    @Override
    public void serialize(ModelAndView value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeNull();
        } else {
            Info info = new Info();
            info.setViewName(value.getViewName());
            if (value.getView() != null)
                info.setView(value.getView().toString());

            if (value.getModel() != null && !value.getModel().isEmpty()) {
                ModelInfo modelInfo = new ModelInfo();
                value.getModel().keySet().stream().filter(name -> !name.startsWith(BindingResult.MODEL_KEY_PREFIX)).forEach(name -> {
                    Object attributeValue = value.getModel().get(name);
                    modelInfo.setName(name);
                    if (attributeValue != null)
                        modelInfo.setValue(attributeValue.toString());

                    Errors errors = (Errors) value.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
                    if (errors != null) {
                        modelInfo.setErrors(errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.toList()));
                    }
                });
                info.setModel(modelInfo);
            }

            gen.writeObject(info);
        }
    }

    @Data
    private class Info {
        private String viewName;
        private String view;
        private ModelInfo model;
    }

    @Data
    private class ModelInfo {
        private String name;
        private String value;
        private List<String> errors;
    }
}
