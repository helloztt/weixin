package me.jiangcai.wx.model;

/**
 * <a href="http://mp.weixin.qq.com/wiki/home/index.html">手册</a>里查找自定义菜单
 *
 * @author CJ
 */
public enum MenuType {
    /**
     * 这是一个特有的类型,表示自己其实什么都不干,仅仅显示下级菜单
     */
    parent,
    click("key"),
    view("url"),
    scancode_push("key"),
    scancode_waitmsg("key"),
    pic_sysphoto("key"),
    pic_photo_or_album("key"),
    pic_weixin("key"),
    location_select("key"),
    media_id("media_id"),
    view_limited("media_id");

    private final String dataName;

    MenuType(String dataName) {
        this.dataName = dataName;
    }

    MenuType() {
        this(null);
    }

    public String getDataName() {
        return dataName;
    }
}
