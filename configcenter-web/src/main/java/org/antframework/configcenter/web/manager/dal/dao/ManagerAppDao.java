/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:03 创建
 */
package org.antframework.configcenter.web.manager.dal.dao;

import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Map;

/**
 * 管理员-应用实体dao
 */
@RepositoryDefinition(domainClass = ManagerApp.class, idClass = Long.class)
public interface ManagerAppDao {

    void save(ManagerApp managerApp);

    void delete(ManagerApp managerApp);

    ManagerApp findByUsernameAndAppCode(String username, String appCode);

    Page<ManagerApp> query(Map<String, Object> searchParams, Pageable pageable);
}
