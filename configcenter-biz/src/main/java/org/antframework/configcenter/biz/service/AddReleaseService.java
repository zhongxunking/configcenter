/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 21:06 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.AppUtils;
import org.antframework.configcenter.biz.util.PropertyValueUtils;
import org.antframework.configcenter.biz.util.RefreshUtils;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.ReleaseDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.Release;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.AddReleaseOrder;
import org.antframework.configcenter.facade.result.AddReleaseResult;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.Scope;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceAfter;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增发布服务
 */
@Service(enableTx = true)
public class AddReleaseService {
    // 配置版本在附件中的key
    private static final String VERSION_KEY = "version";
    // info转换器
    private static final Converter<Release, ReleaseInfo> INFO_CONVERTER = new FacadeUtils.DefaultConverter<>(ReleaseInfo.class);

    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ReleaseDao releaseDao;

    @ServiceBefore
    public void before(ServiceContext<AddReleaseOrder, AddReleaseResult> context) {
        AddReleaseOrder order = context.getOrder();
        // 生成版本
        long version = AppUtils.produceReleaseVersion(order.getAppId());
        context.setAttachmentAttr(VERSION_KEY, version);
    }

    @ServiceExecute
    public void execute(ServiceContext<AddReleaseOrder, AddReleaseResult> context) {
        AddReleaseOrder order = context.getOrder();
        AddReleaseResult result = context.getResult();
        long version = context.getAttachmentAttr(VERSION_KEY);
        // 校验入参
        App app = appDao.findLockByAppId(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        Profile profile = profileDao.findLockByProfileId(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
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
        RefreshUtils.refreshClients(order.getAppId(), order.getProfileId());
    }

    // 构建发布
    private Release buildRelease(AddReleaseOrder order, long version, List<Property> properties) {
        Release release = new Release();
        BeanUtils.copyProperties(order, release);
        release.setVersion(version);
        release.setProperties(properties);

        return release;
    }

    // 构建配置项集合
    private List<Property> buildProperties(AddReleaseOrder order) {
        List<Property> properties = new ArrayList<>();

        List<PropertyValueInfo> propertyValues = PropertyValueUtils.findAppProfilePropertyValues(order.getAppId(), order.getProfileId(), Scope.PRIVATE);
        for (PropertyValueInfo propertyValue : propertyValues) {
            properties.add(new Property(propertyValue.getKey(), propertyValue.getValue(), propertyValue.getScope()));
        }

        return properties;
    }
}
