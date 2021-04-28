package com.mybatis.plug.hanlder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author coco
 * @date 2020-09-15 10:38
 **/
public class DataPermission {

    private Class<? extends DataAccessPermissionHandler> handlerClass;

    private AtomicBoolean flag  = new AtomicBoolean(false);


    public AtomicBoolean getFlag() {
        return flag;
    }

    public void setFlag(AtomicBoolean flag) {
        this.flag = flag;
    }


    public Class<? extends DataAccessPermissionHandler> getHandlerClass() {
        return handlerClass;
    }

    public void setHandlerClass(Class<? extends DataAccessPermissionHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }
}
