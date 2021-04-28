package com.mybatis.plug.annotation;

import java.lang.annotation.*;

/**
 * 默认实现类标示
 * @author coco
 * @date 2020-09-24 10:47
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefaultAutoFilledHandler {
}
