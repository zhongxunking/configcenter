/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-14 16:11 创建
 */
package org.antframework.configcenter.web.controller;

import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.BizConfiguration;
import org.antframework.configcenter.facade.api.ConfigService;
import org.antframework.configcenter.facade.order.FindPropertiesOrder;
import org.antframework.configcenter.facade.result.FindPropertiesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置controller
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private BizConfiguration bizConfiguration;

    /**
     * 查找应用在特定环境中的配置
     *
     * @param mainAppId    主体应用id（必须）
     * @param queriedAppId 被查询配置的应用id（必须）
     * @param profileId    环境id（必须）
     */
    @RequestMapping("/findProperties")
    public FindPropertiesResult findProperties(String mainAppId, String queriedAppId, String profileId) {
        FindPropertiesOrder order = new FindPropertiesOrder();
        order.setMainAppId(mainAppId);
        order.setQueriedAppId(queriedAppId);
        order.setProfileId(profileId);

        return configService.findProperties(order);
    }

    /**
     * 获取配置中心元数据
     */
    @RequestMapping("/meta")
    public MetaResult meta() {
        MetaResult meta = new MetaResult();
        meta.setStatus(Status.SUCCESS);
        meta.setCode(CommonResultCode.SUCCESS.getCode());
        meta.setMessage(CommonResultCode.SUCCESS.getMessage());
        meta.setZkUrls(bizConfiguration.getProperties().getZkUrls().toArray(new String[0]));

        return meta;
    }

    /**
     * 元数据result
     */
    public static class MetaResult extends AbstractResult {
        // 配置中心使用的zookeeper地址
        private String[] zkUrls;

        public String[] getZkUrls() {
            return zkUrls;
        }

        public void setZkUrls(String[] zkUrls) {
            this.zkUrls = zkUrls;
        }
    }
}
