package com.mybatis.plug.annotation;

import com.mybatis.plug.hanlder.DataAccessPermissionHandler;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author coco
 * @date 2020-09-03 14:12
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DataAccessPermission {

    Class<? extends DataAccessPermissionHandler> handler() default DataAccessPermissionHandler.class;
}
