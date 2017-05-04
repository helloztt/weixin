package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author CJ
 */
@Data
public class UserListOpenIds {
    @JsonProperty("openid")
    private List<String> list;
}
