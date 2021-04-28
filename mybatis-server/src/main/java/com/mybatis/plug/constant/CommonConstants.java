package com.mybatis.plug.constant;


import java.io.Serializable;

/**
 * @author lilin
 * @date 2021/03/24
 * 公共常量
 */
public class CommonConstants implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 删除/无效
     */
    public static final Boolean DELETED = false;

    /**
     * 未删除/有效
     */
    public static final Boolean UN_DELETED = true;
    /**
     * 删除-有效
     */
    public static final Integer UN_DELETED_INTEGER = 1;

    /**
     * 删除-有效
     */
    public static final Byte UN_DELETED_BYTE = 1;

    /**
     * true
     */
    public static final Boolean TRUE_FALG = true;

    /**
     * false
     */
    public static final Boolean FALSE_FLAG = false;


    /**
     * 首页规则数量
     */
    public static final Integer HOME_PAGE_RULE_COUNT = 15;

    /**
     * 风险处置名称规则
     */
    public static final String STAT_SUMMARY_NAME_RULE = "\"%s\"%s-%s";


    public static final String RISK_TYPE_IS_NULL = "检查大项为空";
    public static final String RISK_CATEGORY_IS_NULL = "检查小项为空";


}
