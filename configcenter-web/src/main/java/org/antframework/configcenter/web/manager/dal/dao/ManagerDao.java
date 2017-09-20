/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:01 创建
 */
package org.antframework.configcenter.web.manager.dal.dao;

import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Map;

/**
 * 管理员实体dao
 */
@RepositoryDefinition(domainClass = Manager.class, idClass = Long.class)
public interface ManagerDao {

    void save(Manager manager);

    void delete(Manager manager);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Manager findLockByCode(String code);

    Manager findByCode(String code);

    Page<Manager> query(Map<String, Object> searchParams, Pageable pageable);
}
