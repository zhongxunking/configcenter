/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:14 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Map;

/**
 * 应用实体dao
 */
@RepositoryDefinition(domainClass = App.class, idClass = Long.class)
public interface AppDao {

    void save(App app);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    App findLockByAppCode(String appCode);

    App findByAppCode(String appCode);

    void delete(App app);

    Page<App> query(Map<String, Object> searchParams, Pageable pageable);
}
