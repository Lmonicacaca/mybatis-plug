package com.mybatis.plug.pointcut;

import com.mybatis.plug.annotation.DataAccess;
import com.mybatis.plug.annotation.DataAccessPermission;
import com.mybatis.plug.hanlder.DataAccessPermissionHandler;
import com.mybatis.plug.hanlder.DataAccessPermissionHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * 数据权限切面
 * @author coco
 * @date 2020-09-15 10:03
 **/
@Aspect
@Component
public class DataAccessPointCut {

    private static final Logger log = LoggerFactory.getLogger(DataAccessPointCut.class);

    @Pointcut("@annotation(com.mybatis.plug.annotation.DataAccess)")
    public void pointCut() {

    }

    @Before("pointCut()&&@annotation(dataAccess)")
    public void dataAccess(JoinPoint point, DataAccess dataAccess) throws Throwable {
        Class<? extends DataAccessPermissionHandler> handler = dataAccess.handler();
        if (isNotImpl(handler)) {
            log.warn("权限实现类 必须是实现接口的类 ");
            log.info("向上获取服务类 注解");
            DataAccessPermission annotation = point.getTarget().getClass().getAnnotation(DataAccessPermission.class);
            if (Objects.nonNull(annotation)) {
                Class<? extends DataAccessPermissionHandler> handler1 = annotation.handler();
                if (!isNotImpl(handler1)) {
                    DataAccessPermissionHelper.startAccess(handler1);
                }
                log.warn("未成功找到具体权限实现类!");
            }
        } else {
            DataAccessPermissionHelper.startAccess(handler);
        }
    }

    @After("pointCut()&&@annotation(dataAccess)")
    public void after(JoinPoint point, DataAccess dataAccess) throws Throwable{
        DataAccessPermissionHelper.clearLocalDataAccess();
    }

    private boolean isNotImpl(Class handler){
       return handler.isInterface() || Modifier.isAbstract(handler.getModifiers());
    }
}
