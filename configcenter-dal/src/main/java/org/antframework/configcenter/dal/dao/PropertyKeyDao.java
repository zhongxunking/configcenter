/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:46 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.common.constant.CacheConstant;
import org.antframework.configcenter.dal.entity.PropertyKey;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * 配置key dao
 */
@RepositoryDefinition(domainClass = PropertyKey.class, idClass = Long.class)
public interface PropertyKeyDao {
    @CacheEvict(cacheNames = CacheConstant.PROPERTY_KEYS_CACHE_NAME, key = "#p0.appId")
    void save(PropertyKey propertyKey);

    @CacheEvict(cacheNames = CacheConstant.PROPERTY_KEYS_CACHE_NAME, key = "#p0.appId")
    void delete(PropertyKey propertyKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    PropertyKey findLockByAppIdAndKey(String appId, String key);

    @Cacheable(cacheNames = CacheConstant.PROPERTY_KEYS_CACHE_NAME, key = "#p0")
    List<PropertyKey> findByAppId(String appId);
}
