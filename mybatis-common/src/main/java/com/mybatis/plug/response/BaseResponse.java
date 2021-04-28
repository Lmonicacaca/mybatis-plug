package com.mybatis.plug.response;


import java.io.Serializable;

/**
 * @author coco
 * @date 2020-08-05 19:35
 **/
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -5059263952635082252L;

    private Integer code;

    private Boolean success;

    private String message;

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public BaseResponse result(T result) {
        this.result = result;
        return this;
    }

    public BaseResponse code(Integer code) {
        this.setCode(code);
        return this;
    }

    public BaseResponse message(String message) {
        this.setMessage(message);
        return this;
    }

    public BaseResponse success(boolean success) {
        this.setSuccess(success);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> BaseResponse<T> buildBaseResponse(){
        BaseResponse<T> response = new BaseResponse<>();
        return response;
    }


    public static <T> BaseResponse<T> buildSuccessResponse(){
        BaseResponse<T> response = buildBaseResponse();
        response.setSuccess(true);
        response.setCode(200);
        return response;
    }

    public static <T> BaseResponse<T> buildErrorResponse(){
        BaseResponse<T> response = buildBaseResponse();
        response.setSuccess(false);
        return response;
    }
}
