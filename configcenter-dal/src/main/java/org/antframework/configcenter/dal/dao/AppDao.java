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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

/**
 * 应用实体dao
 */
@RepositoryDefinition(domainClass = App.class, idClass = Long.class)
public interface AppDao {

    void save(App app);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    App findLockByAppId(String appId);

    App findByAppId(String appId);

    boolean existsByParent(String parent);

    void delete(App app);

    Page<App> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<App> findAll();
}
