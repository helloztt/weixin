package me.jiangcai.wx.protocol;

import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.MenuType;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

/**
 * @author CJ
 */
public class ProtocolTest {

    private Random random = new Random();

    Protocol protocol;

    @Before
    public void init(){
        protocol = Protocol.forAccount(new me.jiangcai.wx.DebugPublicAccount());
    }

    @Test
    public void createMenu() throws Exception {
        System.out.println(protocol);

        Menu menu1 = randomMenu(true);
        Menu menu2 = randomMenu(true);
        Menu menu3 = randomMenu(true);


        protocol.createMenu(new Menu[]{menu1,menu2,menu3});
    }

    private Menu randomMenu(boolean allowSub) {
        Menu menu = new Menu();
        menu.setName(UUID.randomUUID().toString().substring(0,4));
        if (random.nextBoolean() && allowSub){
            int size = random.nextInt(5)+1;
            Menu[] subs = new Menu[size];
            for (int i = 0; i < size; i++) {
                subs[i] = randomMenu(false);
            }
            menu.setSubs(subs);
        }else{
            menu.setType(MenuType.values()[1+random.nextInt(MenuType.values().length-1)]);
            while (menu.getType().getDataName().equalsIgnoreCase("media_id"))
                menu.setType(MenuType.values()[1+random.nextInt(MenuType.values().length-1)]);
            //  media_id 这个比较麻烦!
            menu.setData("http://www.baidu.com");
        }
        return menu;
    }

}