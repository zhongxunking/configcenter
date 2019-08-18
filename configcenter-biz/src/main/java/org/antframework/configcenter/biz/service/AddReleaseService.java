/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 21:06 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.Refreshes;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.AddReleaseOrder;
import org.antframework.configcenter.facade.result.AddReleaseResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceBefore;
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
 * 新增发布服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddReleaseService {
    // 配置版本在附件中的key
    private static final String VERSION_KEY = "version";
    // info转换器
    private static final Converter<Release, ReleaseInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ReleaseInfo.class);

    // 发布dao
    private final ReleaseDao releaseDao;
    // 应用dao
    private final AppDao appDao;
    // 环境dao
    private final ProfileDao profileDao;

    @ServiceBefore
    public void before(ServiceContext<AddReleaseOrder, AddReleaseResult> context) {
        AddReleaseOrder order = context.getOrder();
        // 生成版本
        long version = Apps.produceReleaseVersion(order.getAppId());
        context.setAttachmentAttr(VERSION_KEY, version);
    }

    @ServiceExecute
    public void execute(ServiceContext<AddReleaseOrder, AddReleaseResult> context) {
        AddReleaseOrder order = context.getOrder();
        AddReleaseResult result = context.getResult();
        long version = context.getAttachmentAttr(VERSION_KEY);
        // 新增发布
        Release release = buildRelease(order, version, buildProperties(order));
        releaseDao.save(release);
        // 设置返回结果
        result.setRelease(INFO_CONVERTER.convert(release));
    }

    @ServiceAfter
    public void after(ServiceContext<AddReleaseOrder, AddReleaseResult> context) {
        AddReleaseOrder order = context.getOrder();
        // 刷新客户端
        Refreshes.refreshClients(order.getAppId(), order.getProfileId());
    }

    // 构建发布
    private Release buildRelease(AddReleaseOrder order, long version, Set<Property> properties) {
        Release release = new Release();
        BeanUtils.copyProperties(order, release);
        release.setVersion(version);
        release.setReleaseTime(new Date());
        release.setProperties(properties);

        return release;
    }

    // 构建配置集
    private Set<Property> buildProperties(AddReleaseOrder order) {
        Map<String, Property> keyProperties = getParentProperties(order).stream().collect(Collectors.toMap(Property::getKey, Function.identity()));
        for (Property property : order.getAddedOrModifiedProperties()) {
            keyProperties.put(property.getKey(), property);
        }
        for (String propertyKey : order.getDeletedPropertyKeys()) {
            keyProperties.remove(propertyKey);
        }

        return new HashSet<>(keyProperties.values());
    }

    // 获取父发布的配置集
    private Set<Property> getParentProperties(AddReleaseOrder order) {
        if (order.getParentVersion() > ReleaseConstant.ORIGIN_VERSION) {
            Release parentRelease = releaseDao.findLockByAppIdAndProfileIdAndVersion(order.getAppId(), order.getProfileId(), order.getParentVersion());
            if (parentRelease == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("父发布[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getParentVersion()));
            }
            return parentRelease.getProperties();
        } else {
            App app = appDao.findLockByAppId(order.getAppId());
            if (app == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
            }
            Profile profile = profileDao.findLockByProfileId(order.getProfileId());
            if (profile == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
            }
            return new HashSet<>();
        }
    }
}
