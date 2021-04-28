package com.mybatis.plug.service;

import com.mybatis.plug.dao.entity.Users;
import com.mybatis.plug.dao.entity.UsersExample;

public interface IUsersService extends IService<Users, UsersExample> {
    Users selectById(Long id);
}