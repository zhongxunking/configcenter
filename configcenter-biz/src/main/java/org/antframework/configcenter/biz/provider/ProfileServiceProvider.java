/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:31 创建
 */
package org.antframework.configcenter.biz.provider;

import org.antframework.boot.bekit.CommonQueries;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindInheritedProfilesResult;
import org.antframework.configcenter.facade.result.FindProfileResult;
import org.antframework.configcenter.facade.result.FindProfileTreeResult;
import org.antframework.configcenter.facade.result.QueryProfilesResult;
import org.bekit.service.ServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 环境服务提供者
 */
@Service
public class ProfileServiceProvider implements ProfileService {
    @Autowired
    private ServiceEngine serviceEngine;

    @Override
    public EmptyResult addOrModifyProfile(AddOrModifyProfileOrder order) {
        return serviceEngine.execute("addOrModifyProfileService", order);
    }

    @Override
    public EmptyResult deleteProfile(DeleteProfileOrder order) {
        return serviceEngine.execute("deleteProfileService", order);
    }

    @Override
    public FindProfileResult findProfile(FindProfileOrder order) {
        return serviceEngine.execute("findProfileService", order);
    }

    @Override
    public FindInheritedProfilesResult findInheritedProfiles(FindInheritedProfilesOrder order) {
        return serviceEngine.execute("findInheritedProfilesService", order);
    }

    @Override
    public FindProfileTreeResult findProfileTree(FindProfileTreeOrder order) {
        return serviceEngine.execute("findProfileTreeService", order);
    }

    @Override
    public QueryProfilesResult queryProfiles(QueryProfilesOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachment(ProfileDao.class));
        return result.convertTo(QueryProfilesResult.class);
    }
}
