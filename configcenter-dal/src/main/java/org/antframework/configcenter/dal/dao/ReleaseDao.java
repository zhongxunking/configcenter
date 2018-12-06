/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-06 22:28 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.Release;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 配置发布dao
 */
@RepositoryDefinition(domainClass = Release.class, idClass = Long.class)
public interface ReleaseDao {

    void save(Release release);

    Release findByAppIdAndProfileIdAndVersion(String appId, String profileId, Long version);
}
