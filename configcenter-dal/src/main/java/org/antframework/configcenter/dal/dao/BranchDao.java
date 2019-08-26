/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-15 23:04 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.Branch;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * 分支dao
 */
@RepositoryDefinition(domainClass = Branch.class, idClass = Long.class)
public interface BranchDao {
    void save(Branch branch);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Branch findLockByAppIdAndProfileIdAndBranchId(String appId, String profileId, String branchId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Branch> findLockByAppIdAndProfileId(String appId, String profileId);

    Branch findByAppIdAndProfileIdAndBranchId(String appId, String profileId, String branchId);
}
