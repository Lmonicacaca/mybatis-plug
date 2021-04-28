package com.mybatis.plug.hanlder;
/**
 * 权限处理类
 * @author coco
 * @date 2020-09-15 09:23
 **/
public interface DataAccessPermissionHandler {

    /**
     *  返回sql
     * @param mapperName 执行的Mapper接口名
     * @return
     */
    String doAccess(String mapperName);
}
