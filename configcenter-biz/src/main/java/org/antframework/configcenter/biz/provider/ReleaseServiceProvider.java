/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 21:04 创建
 */
package org.antframework.configcenter.biz.provider;

import lombok.AllArgsConstructor;
import org.antframework.boot.bekit.CommonQueries;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.AddReleaseResult;
import org.antframework.configcenter.facade.result.FindCurrentReleaseResult;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.bekit.service.ServiceEngine;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布服务提供者
 */
@Service
@AllArgsConstructor
public class ReleaseServiceProvider implements ReleaseService {
    // 服务引擎
    private final ServiceEngine serviceEngine;

    @Override
    public AddReleaseResult addRelease(AddReleaseOrder order) {
        return serviceEngine.execute("addReleaseService", order);
    }

    @Override
    public EmptyResult revertRelease(RevertReleaseOrder order) {
        return serviceEngine.execute("revertReleaseService", order);
    }

    @Override
    public FindCurrentReleaseResult findCurrentRelease(FindCurrentReleaseOrder order) {
        return serviceEngine.execute("findCurrentReleaseService", order);
    }

    @Override
    public FindReleaseResult findRelease(FindReleaseOrder order) {
        return serviceEngine.execute("findReleaseService", order);
    }

    @Override
    public QueryReleasesResult queryReleases(QueryReleasesOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, buildQueryReleasesAttachment());
        return result.convertTo(QueryReleasesResult.class);
    }

    // 构建查询发布的附件
    private Map<Object, Object> buildQueryReleasesAttachment() {
        Map<Object, Object> attachment = new HashMap<>();
        attachment.put(CommonQueries.DAO_CLASS_KEY, ReleaseDao.class);
        attachment.put(CommonQueries.SORT_KEY, new Sort(Sort.Direction.DESC, "version"));

        return attachment;
    }
}
