/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:16 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.*;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.enums.ManagerType;
import org.antframework.configcenter.web.manager.facade.info.ManagerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理管理员的controller
 */
@RestController
@RequestMapping("/manage/manager")
public class ManagerManageController {
    @Autowired
    private ManagerDao managerDao;

    @RequestMapping("/addManager")
    public AbstractResult addManager(String userName, String password, ManagerType type) {
        Manager manager = managerDao.findLockByUserName(userName);
        if (manager != null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.UNKNOWN_ERROR.getCode(), "用户已存在");
        }
        manager = new Manager();
        manager.setUserName(userName);
        manager.setPassword(password);
        manager.setType(type);
        managerDao.save(manager);
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/deleteManager")
    public AbstractResult deleteManager(String userName) {
        Manager manager = managerDao.findLockByUserName(userName);
        if (manager != null) {
            managerDao.delete(manager);
        }
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/modifyManagerType")
    public AbstractResult modifyManagerType(String username, ManagerType type) {
        Manager manager = managerDao.findLockByUserName(username);
        if (manager == null) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.UNKNOWN_ERROR.getCode(), "用户不存在");
        }
        manager.setType(type);
        managerDao.save(manager);
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/queryManager")
    public AbstractQueryResult<ManagerInfo> queryManager(int pageNo, int pageSize, String userName, ManagerType type) {
        if (pageNo < 1 || pageSize < 1) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), CommonResultCode.INVALID_PARAMETER.getMessage());
        }
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_userName", "%" + userName + "%");
        searchParams.put("EQ_type", type);
        Page<Manager> page = managerDao.query(searchParams, new PageRequest(pageNo - 1, pageSize));
        AbstractQueryResult<ManagerInfo> result = new AbstractQueryResult<ManagerInfo>() {
        };
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        FacadeUtils.setQueryResult(result, new FacadeUtils.SpringDataPageExtractor<>(page));
        return result;
    }
}
