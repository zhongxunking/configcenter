/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:14 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.common.util.query.QueryParam;
import org.antframework.configcenter.dal.entity.App;
import org.antframework.configcenter.facade.vo.CacheConstant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

/**
 * 应用dao
 */
@RepositoryDefinition(domainClass = App.class, idClass = Long.class)
public interface AppDao {
    @CacheEvict(cacheNames = CacheConstant.APP_CACHE_NAME, key = "#p0.appId")
    void save(App app);

    @CacheEvict(cacheNames = CacheConstant.APP_CACHE_NAME, key = "#p0.appId")
    void delete(App app);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    App findLockByAppId(String appId);

    @Cacheable(cacheNames = CacheConstant.APP_CACHE_NAME, key = "#p0")
    App findByAppId(String appId);

    List<App> findByParent(String parent);

    boolean existsByParent(String parent);

    Page<App> query(Collection<QueryParam> queryParams, Pageable pageable);
}
