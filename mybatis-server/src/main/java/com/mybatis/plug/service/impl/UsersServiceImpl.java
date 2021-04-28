package com.mybatis.plug.service.impl;

import com.mybatis.plug.dao.entity.Users;
import com.mybatis.plug.dao.entity.UsersExample;
import com.mybatis.plug.dao.mapper.UsersMapper;
import com.mybatis.plug.service.IUsersService;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends ServiceImpl<Users, UsersExample, UsersMapper> implements IUsersService {
    @Override
    public Users selectById(Long id) {
        return this.baseMapper.selectById(id);
    }
}