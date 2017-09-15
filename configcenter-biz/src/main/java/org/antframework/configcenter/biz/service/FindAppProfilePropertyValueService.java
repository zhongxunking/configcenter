/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-15 18:26 创建
 */
package org.antframework.configcenter.biz.service;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.AppDao;
import org.antframework.configcenter.dal.dao.ProfileDao;
import org.antframework.configcenter.dal.dao.PropertyValueDao;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.dal.entity.Profile;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.manage.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.result.manage.FindAppProfilePropertyValueResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceCheck;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用在指定环境的所有属性value服务
 */
@Service
public class FindAppProfilePropertyValueService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private PropertyValueDao propertyValueDao;

    @ServiceCheck
    public void check(ServiceContext<FindAppProfilePropertyValueOrder, FindAppProfilePropertyValueResult> context) {
        FindAppProfilePropertyValueOrder order = context.getOrder();

        App app = appDao.findByAppCode(order.getAppCode());
        if (app == null) {
            throw new AntBekitException(Status.SUCCESS, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppCode()));
        }
        Profile profile = profileDao.findByProfileCode(order.getProfileCode());
        if (profile == null) {
            throw new AntBekitException(Status.SUCCESS, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileCode()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindAppProfilePropertyValueOrder, FindAppProfilePropertyValueResult> context) {
        FindAppProfilePropertyValueOrder order = context.getOrder();
        FindAppProfilePropertyValueResult result = context.getResult();

        List<PropertyValue> propertyValues = propertyValueDao.findByProfileCodeAndAppCode(order.getProfileCode(), order.getAppCode());
        result.setInfos(buildInfos(propertyValues));
    }

    // 构建infos
    private List<PropertyValueInfo> buildInfos(List<PropertyValue> propertyValues) {
        List<PropertyValueInfo> infos = new ArrayList<>();
        for (PropertyValue propertyValue : propertyValues) {
            PropertyValueInfo info = new PropertyValueInfo();
            BeanUtils.copyProperties(propertyValue, info);
            infos.add(info);
        }
        return infos;
    }
}
