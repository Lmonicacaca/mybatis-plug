package com.mybatis.plug;

import com.alibaba.fastjson.JSONObject;
import com.mybatis.plug.dao.entity.ProCompleteCancel;
import com.mybatis.plug.dao.entity.Users;
import com.mybatis.plug.response.Page;
import com.mybatis.plug.service.IProCompleteCancelService;
import com.mybatis.plug.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Optional;

@SpringBootTest(classes = MybatisServerApplication.class)
@Slf4j
class MybatisCoreApplicationTests {
    @Resource
    private IUsersService usersService;
    @Resource
    private IProCompleteCancelService proCompleteCancelService;

    @Test
    void contextLoads() {
        Users users = usersService.queryByKey(10701L).get();
        log.info("修改前："+JSONObject.toJSONString(users));
        users.setRealName("六六六");
        usersService.updateByKey(users);
        log.info("修改后："+JSONObject.toJSONString(users));
        Page<ProCompleteCancel> page = proCompleteCancelService.page();


        Users test = usersService.selectById(10701L);
        log.info(JSONObject.toJSONString(test));
    }



}
