package com.mybatis.plug.hanlder;



import java.util.Objects;

/**
 * @author coco
 * @date 2020-09-15 09:49
 **/
public class DataAccessPermissionHelper {

    protected static final ThreadLocal<DataPermission> LOCAL_DATA_ACCESS = new ThreadLocal<DataPermission>();

    public static void startAccess(Class<? extends DataAccessPermissionHandler> handlerClass) {
        DataPermission dataAccess = new DataPermission();
        dataAccess.setHandlerClass(handlerClass);
        setLocalDataAccess(dataAccess);
    }

    public static void startAccess() {
        DataPermission localDataAccess = getLocalDataAccess();
        if (Objects.nonNull(localDataAccess)) {
            localDataAccess.getFlag().set(true);
            setLocalDataAccess(localDataAccess);
        }
    }

    public static void stopAccess() {
        DataPermission localDataAccess = getLocalDataAccess();
        if (Objects.nonNull(localDataAccess)) {
            localDataAccess.getFlag().set(false);
            setLocalDataAccess(localDataAccess);
        }
    }

    public static boolean isActive() {
        DataPermission localDataAccess = getLocalDataAccess();
        if (Objects.nonNull(localDataAccess)) {
            return localDataAccess.getFlag().get();
        } else {
            return false;
        }
    }

    /**
     * 设置 DataAccess 参数
     *
     * @param dataAccess
     */
    protected static void setLocalDataAccess(DataPermission dataAccess) {
        LOCAL_DATA_ACCESS.set(dataAccess);
    }

    /**
     * 获取 DataAccess 参数
     *
     * @return
     */
    public static DataPermission getLocalDataAccess() {
        return LOCAL_DATA_ACCESS.get();
    }

    /**
     * 移除本地变量
     */
    public static void clearLocalDataAccess() {
        LOCAL_DATA_ACCESS.remove();
    }

}
