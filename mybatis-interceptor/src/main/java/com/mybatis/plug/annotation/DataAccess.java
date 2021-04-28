package com.mybatis.plug.annotation;


import com.mybatis.plug.hanlder.DataAccessPermissionHandler;

import java.lang.annotation.*;

/**
 * @author coco
 * @date 2020-09-14 21:03
 * 需要数据权限的方法注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataAccess {

    String[] value() default "";

    Class<? extends DataAccessPermissionHandler> handler() default DataAccessPermissionHandler.class;
}
