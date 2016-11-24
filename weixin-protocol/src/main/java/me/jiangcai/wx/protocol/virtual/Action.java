package me.jiangcai.wx.protocol.virtual;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 对公众号发起的操作
 *
 * @author CJ
 */
@Data
@AllArgsConstructor
public class Action implements Comparable<Action> {

    private LocalDateTime time;
    private Method method;
    private Object[] arguments;

    @Override
    public int compareTo(Action o) {
        return time.compareTo(o.getTime());
    }
}
