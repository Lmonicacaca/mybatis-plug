package com.mybatis.plug.hanlder;

import com.mybatis.plug.enums.FilledType;

/**
 *  自动填充字段 逻辑填充的入口
 * @author coco
 * @date 2020-09-23 15:21
 **/
public interface FieldAutoFillHanlder {


    /**
     *  返回具体填充字段的值
     * @param className 变更的实体类型
     * @param fieldName 设置的字段名称
     * @param filledType insert or update
     * @return
     */
    Object doFill(Class className,String fieldName, FilledType filledType);
}
