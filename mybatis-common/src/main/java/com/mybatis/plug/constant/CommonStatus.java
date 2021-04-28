package com.mybatis.plug.constant;

/**
 * @author coco
 * @date 2020-09-18 16:36
 **/
public enum CommonStatus {
    PARAM_ERROR(400,"参数错误"),PARAM_REQUIRED(401,"缺少参数"),
    PARAM_NULL(402,"必要参数有空");

    private Integer code;

    private String message;

    CommonStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
