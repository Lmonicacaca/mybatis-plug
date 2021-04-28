package com.mybatis.plug.util;

import com.zpaas.utils.Md5Util;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author coco
 * @date 2020-09-15 17:38
 **/
@Component("springApplicationContextUtil")
public class SpringApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getBean(Class<T> cls) {
        return context.getBean(cls);
    }

    public static <T> T getBeanByName(String name, Class<T> cls) {
        return context.getBean(name, cls);
    }

    public boolean containsBean(String name) {
        return context.containsBean(name);
    }

    public static Environment getEnvironment() {
        return context.getEnvironment();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
