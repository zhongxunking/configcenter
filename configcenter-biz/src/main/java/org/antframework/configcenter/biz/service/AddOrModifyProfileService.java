/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:33 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.facade.order.AddOrModifyProfileOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * 添加或修改环境服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyProfileService {
    // 环境dao
    private final ProfileDao profileDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyProfileOrder, EmptyResult> context) {
        AddOrModifyProfileOrder order = context.getOrder();
        // 校验是否出现循环继承和祖先是否存在
        checkCycleAndAncestor(order.getProfileId(), order.getParent());
        // 新增或修改环境
        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            profile = new Profile();
        }
        BeanUtils.copyProperties(order, profile);
        profileDao.save(profile);
    }

    // 校验是否出现循环继承和祖先是否存在
    private void checkCycleAndAncestor(String profileId, String ancestorId) {
        StringBuilder builder = new StringBuilder(profileId);
        while (ancestorId != null) {
            builder.append("-->").append(ancestorId);
            if (Objects.equals(ancestorId, profileId)) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("出现循环继承[%s]", builder.toString()));
            }
            Profile ancestor = profileDao.findLockByProfileId(ancestorId);
            if (ancestor == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", ancestorId));
            }
            ancestorId = ancestor.getParent();
        }
    }

    @ServiceAfter
    public void after(ServiceContext<AddOrModifyProfileOrder, EmptyResult> context) {
        AddOrModifyProfileOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(null, order.getProfileId());
    }
}
