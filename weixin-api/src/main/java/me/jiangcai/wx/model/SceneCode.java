package me.jiangcai.wx.model;

import lombok.Data;

/**
 * 场景二维码
 *
 * @author CJ
 */
@Data
public class SceneCode {

    /**
     * 第三方提供的二维码图片下载地址一般为https
     */
    private String imageURL;
    /**
     * 这个二维码实际指向的地址,可以以此自行构造二维码
     */
    private String url;

}
