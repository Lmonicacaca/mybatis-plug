package com.mybatis.plug.interceptor;

import com.mybatis.plug.hanlder.DataAccessPermissionHandler;
import com.mybatis.plug.hanlder.DataAccessPermissionHelper;
import com.mybatis.plug.util.SpringApplicationContextUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.io.StringReader;
import java.sql.Connection;
import java.util.Properties;

/**
 * mybatis 数据权限拦截器  只针对select进行处理
 * @author coco
 * @date 2020-09-14 17:27
 **/

@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class,Integer.class})})
public class DataPermissionInterceptor implements Interceptor {

    CCJSqlParserManager parserManager = new CCJSqlParserManager();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler)invocation.getTarget();
        // 由于mappedStatement中有我们需要的方法id,但却是protected的，所以要通过反射获取
        MetaObject statementHandler = SystemMetaObject.forObject(handler);
        MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
        //获取sql
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();
        //获取方法id
        String id = mappedStatement.getId();
        String mapperName = id.substring(0, id.lastIndexOf("."));
        //获得方法类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (DataAccessPermissionHelper.isActive()) {
            //增强sql代码块
            if ("SELECT".equals(sqlCommandType.name())) {
                //如果是select就将sql转成SELECT对象
                Select select = (Select)parserManager.parse(new StringReader(sql));
                //访问各个v\
                // \isitor
                //select.getSelectBody().accept(new SelectVisitorImpl());
                PlainSelect plain = (PlainSelect) select.getSelectBody();
                DataAccessPermissionHandler bean = SpringApplicationContextUtil.getBean(DataAccessPermissionHelper.getLocalDataAccess().getHandlerClass());
                String s = bean.doAccess(mapperName);
                StringBuffer whereSql = new StringBuffer();
                whereSql.append(s);
                //将增强后的sql放回
                Expression where = plain.getWhere();
                if (where == null) {
                    if (whereSql.length() > 0) {
                        Expression expression = CCJSqlParserUtil
                                .parseCondExpression(whereSql.toString());
                        plain.setWhere(expression);
                    }
                } else {
                    if (whereSql.length() > 0) {
                        //where条件之前存在，需要重新进行拼接
                        whereSql.append(" and  " + where.toString() + " ");
                    } else {
                        //新增片段不存在，使用之前的sql
                        whereSql.append(where.toString());
                    }
                    Expression expression = CCJSqlParserUtil
                            .parseCondExpression(whereSql.toString());
                    plain.setWhere(expression);
                }
                String s1 = select.toString();
                //String s1 = select.toString() + " and " + s;
                statementHandler.setValue("delegate.boundSql.sql",s1);
            }

        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
