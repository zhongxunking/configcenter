/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:35 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.common.util.query.QueryParam;
import org.antframework.configcenter.common.constant.CacheConstant;
import org.antframework.configcenter.dal.entity.Profile;
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
 * 环境dao
 */
@RepositoryDefinition(domainClass = Profile.class, idClass = Long.class)
public interface ProfileDao {
    @CacheEvict(cacheNames = CacheConstant.PROFILE_CACHE_NAME, key = "#p0.profileId")
    void save(Profile profile);

    @CacheEvict(cacheNames = CacheConstant.PROFILE_CACHE_NAME, key = "#p0.profileId")
    void delete(Profile profile);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Profile findLockByProfileId(String profileId);

    @Cacheable(cacheNames = CacheConstant.PROFILE_CACHE_NAME, key = "#p0")
    Profile findByProfileId(String profileId);

    List<Profile> findByParent(String parent);

    boolean existsByParent(String parent);

    Page<Profile> query(Collection<QueryParam> queryParams, Pageable pageable);
}
