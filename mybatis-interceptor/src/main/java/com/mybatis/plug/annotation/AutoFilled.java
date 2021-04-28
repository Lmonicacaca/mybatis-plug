package com.mybatis.plug.annotation;

import com.mybatis.plug.enums.FilledType;
import com.mybatis.plug.hanlder.FieldAutoFillHanlder;

import java.lang.annotation.*;

/**
 * @author coco
 * @date 2020-09-23 14:53
 * 需要自动更新的字段注解
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoFilled {

    /**
     * 定制的设置值逻辑
     * @return
     */
    Class<? extends FieldAutoFillHanlder> fillClass() default FieldAutoFillHanlder.class;

    /**
     *  何时需要填充值
     * @return
     */
    FilledType[] fill() default {FilledType.INSERT,FilledType.UPDATE};
}
