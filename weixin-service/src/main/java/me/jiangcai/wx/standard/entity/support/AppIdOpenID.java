package me.jiangcai.wx.standard.entity.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 微信帐号的主键
 * 如果应用中对appId并不关注可以选择忽略
 *
 * @author CJ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppIdOpenID implements Serializable {

    private static final long serialVersionUID = 749375213841872568L;

    @Column(length = 30)
    private String appId;
    @Column(length = 30)
    private String openId;

    public AppIdOpenID(String openId) {
        this.openId = openId;
    }

}
