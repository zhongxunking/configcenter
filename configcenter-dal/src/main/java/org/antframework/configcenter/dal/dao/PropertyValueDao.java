/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:41 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.PropertyValue;

/**
 *
 */
public interface PropertyValueDao {

    void save(PropertyValue propertyValue);

    PropertyValue findLockByProfileCodeAndAppCodeAndKey(String profileCode, String appCode, String key);

    void delete(PropertyValue propertyValue);
}
