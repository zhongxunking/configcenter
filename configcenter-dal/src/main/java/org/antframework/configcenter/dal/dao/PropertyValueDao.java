/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:41 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.common.constant.CacheConstant;
import org.antframework.configcenter.dal.entity.PropertyValue;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * 配置value dao
 */
@RepositoryDefinition(domainClass = PropertyValue.class, idClass = Long.class)
public interface PropertyValueDao {
    @CacheEvict(cacheNames = CacheConstant.PROPERTY_VALUES_CACHE_NAME, key = "#p0.appId + ',' + #p0.profileId + ',' + #p0.branchId")
    void save(PropertyValue propertyValue);

    @CacheEvict(cacheNames = CacheConstant.PROPERTY_VALUES_CACHE_NAME, key = "#p0.appId + ',' + #p0.profileId + ',' + #p0.branchId")
    void delete(PropertyValue propertyValue);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    PropertyValue findLockByAppIdAndProfileIdAndBranchIdAndKey(String appId, String profileId, String branchId, String key);

    @Cacheable(cacheNames = CacheConstant.PROPERTY_VALUES_CACHE_NAME, key = "#p0 + ',' + #p1 + ',' + #p2")
    List<PropertyValue> findByAppIdAndProfileIdAndBranchId(String appId, String profileId, String branchId);
}
