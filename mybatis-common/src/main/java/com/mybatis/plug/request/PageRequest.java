package com.mybatis.plug.request;

import java.io.Serializable;

/**
 * @author coco
 * @date 2020-08-05 18:47
 **/
public class PageRequest implements Serializable {

    public static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final long serialVersionUID = -2270702224701220188L;

    /**
     * 当前页号
     */
    private int pageNum;

    /**
     * 每页数量
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
