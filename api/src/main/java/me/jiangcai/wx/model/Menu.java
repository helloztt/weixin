package me.jiangcai.wx.model;

import lombok.Data;

/**
 * @author CJ
 */
@Data
public class Menu {

    private MenuType type;
    // sub_button 只有parent才会有
    private Menu[] subs;
    private String name;
    /**
     * 数据,不同的type会有不同的展示
     * 比如key,url,media_id
     */
    private String data;

    public static String toContent(Menu[] menus) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"button\":[");
        for (int i = 0; i < menus.length; i++) {
            Menu menu = menus[i];
            if (i > 0)
                builder.append(',');
            toContent(builder, menu);
        }
        builder.append("]}");
        return builder.toString();
    }

    private static void toContent(StringBuilder builder, Menu menu) {
        builder.append("{");
        builder.append("\"name\":\"").append(menu.name).append("\"");
        if (menu.getType() == null || menu.getType() == MenuType.parent) {
            // sub
            if (menu.getSubs() != null) {
                builder.append(",\"sub_button\": [");
                for (int i = 0; i < menu.getSubs().length; i++) {
                    Menu sub = menu.getSubs()[i];
                    if (i > 0)
                        builder.append(',');
                    toContent(builder, sub);
                }
                builder.append("]");
            }

        } else {

            assert menu.data != null;
            //
            builder.append(",\"type\":\"");
            builder.append(menu.type.name()).append("\"");
            //
            builder.append(",\"").append(menu.type.getDataName()).append("\":\"");
            builder.append(menu.data).append("\"");
        }
        builder.append("}");
    }
}
