package com.mybatis.plug.response;

import com.mybatis.plug.request.PageRequest;

import java.io.Serializable;
import java.util.List;

/**
 * @author coco
 * @date 2020-08-05 18:48
 **/
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -4648928231003870530L;
    private long total;

    private int pageNum = 1;

    private int pageSize = PageRequest.DEFAULT_PAGE_SIZE;

    private List<T> results;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

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

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public static <T> Page<T> buildFromRequest(PageRequest request, Class<T> clazz){
        Page<T> response = new Page<>();
        response.setPageNum(request.getPageNum());
        response.setPageSize(request.getPageSize());
        return response;
    }

}
