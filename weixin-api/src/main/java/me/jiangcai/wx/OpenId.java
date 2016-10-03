package me.jiangcai.wx;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注这个数据应该是当前openId,而且必须获取,如果没有值则程序会跑错
 * 你可以在Spring MVC Controller参数上直接引入,但必须注意同时不可以有state,code参数
 *
 * @author CJ
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenId {
}
