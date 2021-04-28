package com.mybatis.plug.handle;

import com.mybatis.plug.hanlder.DataAccessPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 分页查询(单表查询)-数据权限(以项目为基础)（只支持-自己、本院、全部）
 *
 * @author zhangl
 * @date 2020-09-21 15:06
 **/
@Component
@Slf4j
public class PageQueryDataAccessPermissionHandler implements DataAccessPermissionHandler {


    @Override
    public String doAccess(String mapperName) {

        String sql = dataAccessSql("", mapperName);
        return sql;

    }

    /**
     * <p>
     * 作为数据数据权限的字段 如果多表联合查询  需要加上表前缀
     * </p>
     *
     * @param mainTablePreFix 多表联合查询  主表前缀
     * @return
     */
    public String dataAccessSql(String mainTablePreFix, String mapperName) {

        // 获取当前登录人的数据权限dataPermission和用户id

        String projectIdStr = "project_id";
        if (StringUtils.isNotBlank(mapperName) && mapperName.endsWith("ProBaseInfoMapper")) {
            projectIdStr = "id";
        }

        //当前登录人关联项目
        String currentUserOneSelect = String.format(" %s%s in (select project_id from yj_pro_user_rela where user_id = %s and deleted = 1)", mainTablePreFix, projectIdStr, 3);
        String sql = currentUserOneSelect;

        log.info("数据权限 【sql:{}】",sql);
        return sql;
    }
}
