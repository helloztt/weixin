package me.jiangcai.wx.protocol.impl;

import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.exception.BadAccessException;
import me.jiangcai.wx.protocol.virtual.Action;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
public class ProtocolCallback implements InvocationHandler {

    private final ProtocolImpl protocol;

    public ProtocolCallback(PublicAccount account) {
        protocol = new ProtocolImpl(account);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        // 如果这个方法是Protocol 声明的;那么就需要被保护
        try {
            Protocol.class.getMethod(method.getName(), (Class[]) method.getParameterTypes());

            if (Protocol.VirtualAppID.equals(protocol.getAccount().getAppID())) {
                final Action action = new Action(LocalDateTime.now(), method, objects);
                System.out.println(action);
                Protocol.virtualActions.add(action);
                return null;
            }

            if (protocol.getAccount().getAccessToken() == null || protocol.getAccount().getTimeToExpire().isBefore(LocalDateTime.now())) {
                protocol.newAccessToken();
            }

            try {
                return method.invoke(protocol, objects);
            } catch (BadAccessException ex) {
                protocol.newAccessToken();
                return method.invoke(protocol, objects);
            } catch (InvocationTargetException exception) {
                throw exception.getTargetException();
            }

        } catch (NoSuchMethodException ex) {
            return method.invoke(protocol, objects);
        }
    }
}
