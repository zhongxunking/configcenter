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
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.facade.api.ReleaseService;
import org.antframework.configcenter.facade.order.FindReleaseOrder;
import org.antframework.configcenter.facade.order.QueryReleasesOrder;
import org.antframework.configcenter.facade.result.FindReleaseResult;
import org.antframework.configcenter.facade.result.QueryReleasesResult;
import org.bekit.service.ServiceEngine;
import org.springframework.stereotype.Service;

/**
 * 发布服务提供者
 */
@Service
@AllArgsConstructor
public class ReleaseServiceProvider implements ReleaseService {
    // 服务引擎
    private final ServiceEngine serviceEngine;

    @Override
    public FindReleaseResult findRelease(FindReleaseOrder order) {
        return serviceEngine.execute("findReleaseService", order);
    }

    @Override
    public QueryReleasesResult queryReleases(QueryReleasesOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachment(ReleaseDao.class));
        return result.convertTo(QueryReleasesResult.class);
    }
}
