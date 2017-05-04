package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author CJ
 */
@Data
public class UserList {
    private long total;
    private int count;
    private UserListOpenIds data;
    @JsonProperty("next_openid")
    private String next;

}
