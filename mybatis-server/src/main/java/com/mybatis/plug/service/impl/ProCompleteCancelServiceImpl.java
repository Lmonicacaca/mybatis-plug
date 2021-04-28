package com.mybatis.plug.service.impl;

import com.mybatis.plug.annotation.DataAccess;
import com.mybatis.plug.annotation.DataAccessPermission;
import com.mybatis.plug.dao.entity.ProCompleteCancel;
import com.mybatis.plug.dao.entity.ProCompleteCancelExample;
import com.mybatis.plug.dao.mapper.ProCompleteCancelMapper;
import com.mybatis.plug.handle.PageQueryDataAccessPermissionHandler;
import com.mybatis.plug.hanlder.DataAccessPermissionHandler;
import com.mybatis.plug.hanlder.DataAccessPermissionHelper;
import com.mybatis.plug.request.PageRequest;
import com.mybatis.plug.response.Page;
import com.mybatis.plug.service.IProCompleteCancelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProCompleteCancelServiceImpl extends ServiceImpl<ProCompleteCancel, ProCompleteCancelExample, ProCompleteCancelMapper> implements IProCompleteCancelService {

    @Override
    @DataAccess(handler = PageQueryDataAccessPermissionHandler.class)
    public Page<ProCompleteCancel> page() {
        Page page = Page.buildFromRequest(new PageRequest(),ProCompleteCancel.class);
        DataAccessPermissionHelper.startAccess();
        List<ProCompleteCancel> proCompleteCancels = this.baseMapper.selectByExample(new ProCompleteCancelExample());
        page.setResults(proCompleteCancels);
        DataAccessPermissionHelper.stopAccess();
        return page;
    }
}