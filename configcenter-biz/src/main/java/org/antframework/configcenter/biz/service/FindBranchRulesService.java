/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-01 20:03 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.dal.dao.BranchRuleDao;
import org.antframework.configcenter.dal.entity.BranchRule;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.BranchRuleInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.FindBranchRulesOrder;
import org.antframework.configcenter.facade.result.FindBranchRulesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * 查找分支规则
 */
@Service
@AllArgsConstructor
public class FindBranchRulesService {
    // 转换器
    private static final Converter<BranchRule, BranchRuleInfo> CONVERTER = new FacadeUtils.DefaultConverter<>(BranchRuleInfo.class);

    // 分支规则dao
    private final BranchRuleDao branchRuleDao;

    @ServiceBefore
    public void before(ServiceContext<FindBranchRulesOrder, FindBranchRulesResult> context) {
        FindBranchRulesOrder order = context.getOrder();
        // 校验
        AppInfo app = Apps.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        ProfileInfo profile = Profiles.findProfile(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindBranchRulesOrder, FindBranchRulesResult> context) {
        FindBranchRulesOrder order = context.getOrder();
        FindBranchRulesResult result = context.getResult();
        // 查找分支规则
        List<BranchRule> branchRules = branchRuleDao.findByAppIdAndProfileIdOrderByPriorityAsc(order.getAppId(), order.getProfileId());
        branchRules.stream()
                .map(CONVERTER::convert)
                .forEach(result::addBranchRule);
    }
}
