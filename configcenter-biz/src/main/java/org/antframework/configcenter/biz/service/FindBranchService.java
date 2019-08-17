/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.order.FindBranchOrder;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

/**
 * 查找分支服务
 */
@Service
@AllArgsConstructor
public class FindBranchService {
    // info转换器
    private static final Converter<Branch, BranchInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(BranchInfo.class);

    // 分支dao
    private final BranchDao branchDao;

    @ServiceExecute
    public void execute(ServiceContext<FindBranchOrder, FindBranchResult> context) {
        FindBranchOrder order = context.getOrder();
        FindBranchResult result = context.getResult();

        Branch branch = branchDao.findByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch != null) {
            result.setBranch(INFO_CONVERTER.convert(branch));
        }
    }
}
