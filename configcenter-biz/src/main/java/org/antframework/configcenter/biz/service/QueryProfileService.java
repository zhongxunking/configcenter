/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:37 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.FacadeUtils.SpringDataPageExtractor;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.manage.QueryProfileOrder;
import org.antframework.configcenter.facade.result.manage.QueryProfileResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询环境服务
 */
@Service
public class QueryProfileService {
    @Autowired
    private ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryProfileOrder, QueryProfileResult> context) {
        QueryProfileOrder order = context.getOrder();

        Page<Profile> page = profileDao.query(buildSearchParams(order), buildPageable(order));
        FacadeUtils.setQueryResult(context.getResult(), new SpringDataPageExtractor<>(page));
    }

    // 构建查询条件
    private Map<String, Object> buildSearchParams(QueryProfileOrder queryProfileOrder) {
        Map<String, Object> searchParams = new HashMap<>();
        if (queryProfileOrder.getProfileCode() != null) {
            searchParams.put("LIKE_profileCode", "%" + queryProfileOrder.getProfileCode() + "%");
        }
        return searchParams;
    }

    // 构建分页
    private Pageable buildPageable(QueryProfileOrder queryProfileOrder) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return new PageRequest(queryProfileOrder.getPageNo() - 1, queryProfileOrder.getPageSize(), sort);
    }
}
