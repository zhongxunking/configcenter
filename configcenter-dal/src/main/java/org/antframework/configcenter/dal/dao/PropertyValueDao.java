/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 01:41 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.PropertyValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

/**
 * 属性value实体dao
 */
@RepositoryDefinition(domainClass = PropertyValue.class, idClass = Long.class)
public interface PropertyValueDao {

    void save(PropertyValue propertyValue);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    PropertyValue findLockByProfileCodeAndAppCodeAndKey(String profileCode, String appCode, String key);

    List<PropertyValue> findByProfileCodeAndAppCode(String profileCode, String appCode);

    void delete(PropertyValue propertyValue);

    boolean existsByProfileCode(String profileCode);

    boolean existsByAppCodeAndKey(String appCode, String key);

    Page<PropertyValue> query(Map<String, Object> searchParams, Pageable pageable);
}
