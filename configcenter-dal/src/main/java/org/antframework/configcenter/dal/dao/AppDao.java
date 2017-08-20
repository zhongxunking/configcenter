/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:14 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.configcenter.dal.entity.App;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

/**
 *
 */
public interface AppDao {

    void save(App app);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    App findLockByAppCode(String appCode);

    void delete(App app);
}
