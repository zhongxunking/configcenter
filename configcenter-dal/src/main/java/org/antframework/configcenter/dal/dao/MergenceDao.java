/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-31 22:36 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.Mergence;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * 合并dao
 */
@RepositoryDefinition(domainClass = Mergence.class, idClass = Long.class)
public interface MergenceDao {
    void save(Mergence mergence);

    void delete(Mergence mergence);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Mergence findLockByAppIdAndProfileIdAndReleaseVersion(String appId, String profileId, Long releaseVersion);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Mergence> findLockByAppIdAndProfileIdAndSourceReleaseVersion(String appId, String profileId, Long sourceReleaseVersion);

    Mergence findByAppIdAndProfileIdAndReleaseVersion(String appId, String profileId, Long releaseVersion);
}
