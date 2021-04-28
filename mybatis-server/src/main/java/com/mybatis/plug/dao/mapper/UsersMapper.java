package com.mybatis.plug.dao.mapper;

import com.mybatis.plug.mapper.BaseMapper;
import com.mybatis.plug.dao.entity.Users;
import com.mybatis.plug.dao.entity.UsersExample;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends BaseMapper<Users, UsersExample> {
    Users selectById(@Param("id") Long id);

}