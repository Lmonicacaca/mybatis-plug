package com.mybatis.plug.annotation;

import java.lang.annotation.*;

/**
 * @author coco
 * @date 2020-07-06 17:39
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Logic {

    /**
     * 是否强制必须添加
     * @return
     */
    boolean required() default false;
}
