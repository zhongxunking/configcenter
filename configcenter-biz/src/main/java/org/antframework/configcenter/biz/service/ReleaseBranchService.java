/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-29 23:47 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.converter.BranchConverter;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.order.ReleaseBranchOrder;
import org.antframework.configcenter.facade.order.ReleaseBranchResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 发布分支
 */
@Service(enableTx = true)
@AllArgsConstructor
public class ReleaseBranchService {
    // 转换器
    private static final Converter<Branch, BranchInfo> CONVERTER = new BranchConverter();

    // 分支dao
    private final BranchDao branchDao;
    // 发布dao
    private final ReleaseDao releaseDao;

    @ServiceExecute
    public void execute(ServiceContext<ReleaseBranchOrder, ReleaseBranchResult> context) {
        ReleaseBranchOrder order = context.getOrder();
        ReleaseBranchResult result = context.getResult();
        // 校验
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }
        // 新增发布
        Release release = buildRelease(branch, order);
        releaseDao.save(release);
        // 更新分支
        branch.setReleaseVersion(release.getVersion());
        branchDao.save(branch);
        // 设置result
        result.setBranch(CONVERTER.convert(branch));
    }

    // 构建发布
    private Release buildRelease(Branch branch, ReleaseBranchOrder order) {
        Set<Property> properties = buildProperties(getCurrentProperties(branch), order);

        Release release = new Release();
        BeanUtils.copyProperties(order, release);
        release.setVersion(Apps.produceReleaseVersion(branch.getAppId()));
        release.setReleaseTime(new Date());
        release.setProperties(properties);
        release.setParentVersion(branch.getReleaseVersion());

        return release;
    }

    // 构建配置集
    private Set<Property> buildProperties(Set<Property> parentProperties, ReleaseBranchOrder order) {
        Map<String, Property> keyProperties = parentProperties.stream().collect(Collectors.toMap(Property::getKey, Function.identity()));
        for (Property property : order.getAddOrModifiedProperties()) {
            keyProperties.put(property.getKey(), property);
        }
        for (String propertyKey : order.getRemovedPropertyKeys()) {
            keyProperties.remove(propertyKey);
        }

        return new HashSet<>(keyProperties.values());
    }

    // 获取当前配置集
    private Set<Property> getCurrentProperties(Branch branch) {
        if (branch.getReleaseVersion() == ReleaseConstant.ORIGIN_VERSION) {
            return new HashSet<>();
        }
        Release release = releaseDao.findLockByAppIdAndProfileIdAndVersion(branch.getAppId(), branch.getProfileId(), branch.getReleaseVersion());
        if (release == null) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("发布[appId=%s,profileId=%s,version=%d]不存在", branch.getAppId(), branch.getProfileId(), branch.getReleaseVersion()));
        }
        return release.getProperties();
    }

    @ServiceAfter
    public void after(ServiceContext<ReleaseBranchOrder, ReleaseBranchResult> context) {
        ReleaseBranchOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }
}
