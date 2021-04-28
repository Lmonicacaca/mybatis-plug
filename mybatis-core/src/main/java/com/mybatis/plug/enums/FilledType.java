package com.mybatis.plug.enums;

/**
 * @author coco
 * @date 2020-09-23 15:08
 **/
public enum FilledType {
    INSERT("insert"),UPDATE("update");

    String type;

    FilledType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
