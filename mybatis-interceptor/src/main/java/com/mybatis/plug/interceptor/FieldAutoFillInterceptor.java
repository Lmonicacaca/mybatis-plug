package com.mybatis.plug.interceptor;

import com.alibaba.fastjson.JSON;
import com.mybatis.plug.annotation.AutoFilled;
import com.mybatis.plug.annotation.DefaultAutoFilledHandler;
import com.mybatis.plug.enums.FilledType;
import com.mybatis.plug.hanlder.FieldAutoFillHanlder;
import com.mybatis.plug.util.SpringApplicationContextUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author coco
 * @date 2020-09-23 15:34
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class FieldAutoFillInterceptor implements Interceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FieldAutoFillHanlder.class);


    private static final List<String> CHECKS = new ArrayList<>(2);

    static {
        CHECKS.add("INSERT");
        CHECKS.add("UPDATE");
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //String sql = boundSql.getSql();
        //获得方法类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (CHECKS.contains(sqlCommandType.name())) {
            // 获取参数
            if (LOG.isDebugEnabled()) {
                LOG.debug("进行自动设置字段值");
            }
            Object parameter = invocation.getArgs()[1];
            setFieldAutoFilled(parameter, sqlCommandType.name());
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

    /**
     * 获取实体类 @AutoFilled 的其中一个属性名称
     *
     * @param parameter
     * @return
     */
    protected void setFieldAutoFilled(Object parameter, String sqlType) {
        Field[] fields = parameter.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoFilled.class)) {
                field.setAccessible(true);
                try {
                    if (Objects.isNull(field.get(parameter))) {
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    LOG.warn(String.format("获取方法值失败，fieldName:%s,obj:%s", field.getName(), JSON.toJSONString(parameter)), e);
                }

                AutoFilled annotation = field.getAnnotation(AutoFilled.class);
                // 获取插入类型
                FilledType[] fill = annotation.fill();

                Map<String, FilledType> types = new HashMap<>(2);
                for (FilledType filledType : fill) {
                    types.put(filledType.getType(), filledType);
                }
                String type = sqlType.toLowerCase();
                //如果
                if (types.containsKey(type)) {

                    Class<? extends FieldAutoFillHanlder> fillClass = annotation.fillClass();
                    FieldAutoFillHanlder hanlder = null;

                    Map<String, ? extends FieldAutoFillHanlder> beansOfType = SpringApplicationContextUtil.getApplicationContext().getBeansOfType(fillClass);
                    List<FieldAutoFillHanlder> fieldAutoFillHanlders = new ArrayList<>(beansOfType.values());
                    // 判断是否有多个实现类
                    if (fieldAutoFillHanlders.size() > 1) {
                        // 存在多个实现类 取默认的
                        for (FieldAutoFillHanlder fieldAutoFillHanlder : fieldAutoFillHanlders) {
                            if (fieldAutoFillHanlder.getClass().isAnnotationPresent(DefaultAutoFilledHandler.class)) {
                                hanlder = fieldAutoFillHanlder;
                            }
                        }
                    } else if (fieldAutoFillHanlders.size() > 0) {
                        hanlder = fieldAutoFillHanlders.get(0);
                    }

                    if (Objects.isNull(hanlder)) {
                        LOG.warn("not find FieldAutoFillHanlder impl in the ioc container");
                        continue;
                    }
                    Object o = hanlder.doFill(parameter.getClass(), field.getName(), types.get(type));

                    try {
                        field.set(parameter, o);
                    } catch (Exception e) {
                        LOG.warn("自动设置参数失败，reason：{}", e.getMessage(), e);
                    }
                }


            }
        }
    }

}
