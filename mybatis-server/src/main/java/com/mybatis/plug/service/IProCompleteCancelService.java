package com.mybatis.plug.service;

import com.mybatis.plug.dao.entity.ProCompleteCancel;
import com.mybatis.plug.dao.entity.ProCompleteCancelExample;
import com.mybatis.plug.response.Page;

public interface IProCompleteCancelService extends IService<ProCompleteCancel, ProCompleteCancelExample> {

    Page<ProCompleteCancel> page();
}