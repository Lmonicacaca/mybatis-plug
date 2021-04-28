package com.mybatis.plug.tool;

import com.mybatis.plug.util.SpringApplicationContextUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author coco
 * @date 2020-07-05 00:01
 **/
public final class SqlHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SqlHelper.class);

    public static SqlSessionFactory FACTORY;

    static {
        FACTORY = SpringApplicationContextUtil.getBean(SqlSessionFactory.class);
    }

    public SqlHelper() {
    }

    public static SqlSession sqlSessionBatch() {
        return FACTORY.openSession(ExecutorType.BATCH);
    }

    private static SqlSession getSqlSession() {
        return SqlSessionUtils.getSqlSession(FACTORY);
    }

    public static SqlSession sqlSession() {
        return getSqlSession();
    }


}
