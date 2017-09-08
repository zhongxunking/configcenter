/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:35 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

/**
 * 环境实体dao
 */
@RepositoryDefinition(domainClass = Profile.class, idClass = Long.class)
public interface ProfileDao {

    void save(Profile profile);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Profile findLockByProfileCode(String profileCode);

    Profile findByProfileCode(String profileCode);

    void delete(Profile profile);

    Page<Profile> query(Map<String, Object> searchParams, Pageable pageable);

    List<Profile> findAll();
}
