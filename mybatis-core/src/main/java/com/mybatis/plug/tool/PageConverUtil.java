package com.mybatis.plug.tool;

import com.github.pagehelper.PageInfo;
import com.mybatis.plug.response.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 将pagehelper的PageInfo 转化成 自身框架的Page
 *
 * @author coco
 * @date 2020-09-16 15:28
 **/
public class PageConverUtil {

    private static final Logger log = LoggerFactory.getLogger(PageConverUtil.class);

    public static<T> Page<T> conver(PageInfo pageInfo,Page<T> page, Class<T> clsz) {
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            List<T> collect = (List<T>)pageInfo.getList().stream().map(object -> {
                T entity = null;
                try {
                    entity = clsz.newInstance();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                BeanUtils.copyProperties(object, entity);
                return entity;
            }).collect(Collectors.toList());
            page.setResults(collect);
        }
        page.setTotal(pageInfo.getTotal());
        return page;
    }


    public static<T> Page<T> conver(PageInfo pageInfo,Class<T> clsz) {
        Page<T> page = new Page<>();
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        conver(pageInfo,page,clsz);
        return page;
    }
}
