package com.mybatis.plug.configuration;

import com.mybatis.plug.interceptor.DataPermissionInterceptor;
import com.mybatis.plug.interceptor.FieldAutoFillInterceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author coco
 * @date 2020-09-14 20:07
 **/
@Configuration
public class MybatisConfiguration {

    //将插件加入到mybatis插件拦截链中
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                //插件拦截链采用了责任链模式，执行顺序和加入连接链的顺序有关
                FieldAutoFillInterceptor fieldAutoFillInterceptor = new FieldAutoFillInterceptor();
                configuration.addInterceptor(fieldAutoFillInterceptor);
                DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
                configuration.addInterceptor(dataPermissionInterceptor);
            }
        };
    }

}
