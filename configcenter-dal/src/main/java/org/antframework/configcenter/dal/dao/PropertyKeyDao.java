/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 20:46 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.PropertyKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

/**
 * 属性key实体dao
 */
@RepositoryDefinition(domainClass = PropertyKey.class, idClass = Long.class)
public interface PropertyKeyDao {

    void save(PropertyKey propertyKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    PropertyKey findLockByAppCodeAndKey(String appCode, String key);

    void delete(PropertyKey propertyKey);

    boolean existsByAppCode(String appCode);

    List<PropertyKey> findByAppCode(String appCode);

    Page<PropertyKey> query(Map<String, Object> searchParams, Pageable pageable);
}
