/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:46 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.PropertyKey;

/**
 *
 */
public interface PropertyKeyDao {

    void save(PropertyKey propertyKey);

    PropertyKey findLockByAppCodeAndKey(String appCode, String key);

    void delete(PropertyKey propertyKey);
}
