package com.mybatis.plug.handle;

import com.mybatis.plug.annotation.DefaultAutoFilledHandler;
import com.mybatis.plug.constant.CommonConstants;
import com.mybatis.plug.enums.FilledType;
import com.mybatis.plug.hanlder.FieldAutoFillHanlder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 插入更新公共字段设置
 **/
@Component
@DefaultAutoFilledHandler
@Slf4j
public class CommonAutoFilledHandler implements FieldAutoFillHanlder {

    @Override
    public Object doFill(Class aClass, String fieldName, FilledType filledType) {
        Object value = null;
        // 当前登录人
        Long currentUserId = -1L;
        // 当前时间
        Date currentTime = new Date();

        Class fileType = null;
        if (FilledType.INSERT.equals(filledType)) {
            switch (fieldName) {
                case "deleted":
                case "state":
                    fileType = getCurrentFieldType(Boolean.class, aClass, fieldName);
                    if (Boolean.class.isAssignableFrom(fileType)) {
                        value = CommonConstants.UN_DELETED;
                    } else if (Integer.class.isAssignableFrom(fileType)) {
                        value = CommonConstants.UN_DELETED_INTEGER;
                    } else if (Byte.class.isAssignableFrom(fileType)) {
                        value = CommonConstants.UN_DELETED_BYTE;
                    }
                    break;
                case "gmtModified":
                case "updateTime":
                    value = currentTime;
                    break;
                case "gmtCreate":
                case "createTime":
                    value = currentTime;
                    break;
                case "creator":
                case "createUserId":
                    value = currentUserId;
                    break;
                case "operator":
                case "updateUserId":
                    value = currentUserId;
                    break;
                default:
                    break;
            }
        } else if (FilledType.UPDATE.equals(filledType)) {
            switch (fieldName) {
                case "gmtModified":
                case "updateTime":
                    value = currentTime;
                    break;
                case "operator":
                case "createUserId":
                    value = currentUserId;
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    private Class getCurrentFieldType(Class defaultClazz, Class entityClazz, String fieldName) {
        Class fileType = defaultClazz;
        try {
            Field currentField = entityClazz.getDeclaredField(fieldName);
            fileType = currentField.getType();
        } catch (NoSuchFieldException e) {
            log.info("获取属性类型失败，e:{}", e.getMessage());
        }
        return fileType;
    }
}
