package com.mybatis.plug.constant;

public class CommonError {

    public enum Code {
        //1000 0001 0001  10000代表项目，form 0001 代表模块  0001 具体业务含义
        //100X 代表通用号段
        //200X 代表业务
        UNKNOW_EXCEPTION(1001,"未知异常"),
        PARAM_NOT_NULL(1002,"参数为空"),
        SYSTEM_EXCEPTION(1003,"系统异常,请稍后再试"),

        SYSTEM_ERROR(2001,"业务异常"),
        FILE_NOT_FOUND(100001011, "文件未找到"),
        FILE_SAVE_ERROR(100001012, "文件保存失败"),

        ;
        private int code;
        private String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    
}
