/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-19 21:51 创建
 */
package org.antframework.configcenter.web.controller.manage;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.common.util.facade.*;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.antframework.configcenter.web.manager.facade.info.ManagerAppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理管理员-应用controller
 */
@RestController
@RequestMapping("/manage/managerApp")
public class ManagerAppManageController {
    @Autowired
    private ManagerAppDao managerAppDao;

    @RequestMapping("/addManagerApp")
    public AbstractResult addManagerApp(String userName, String appCode) {
        ManagerApp managerApp = managerAppDao.findByUserNameAndAppCode(userName, appCode);
        if (managerApp == null) {
            managerApp = new ManagerApp();
            managerApp.setUserName(userName);
            managerApp.setAppCode(appCode);
            managerAppDao.save(managerApp);
        }
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/deleteManagerApp")
    public AbstractResult deleteManagerApp(String userName, String appCode) {
        ManagerApp managerApp = managerAppDao.findByUserNameAndAppCode(userName, appCode);
        if (managerApp != null) {
            managerAppDao.delete(managerApp);
        }
        throw new AntBekitException(Status.SUCCESS, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
    }

    @RequestMapping("/queryManagerApp")
    public AbstractQueryResult queryManagerApp(int pageNo, int pageSize, String userName, String appCode) {
        if (pageNo < 1 || pageSize < 1) {
            throw new AntBekitException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), CommonResultCode.INVALID_PARAMETER.getMessage());
        }
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_userName", "%" + userName + "%");
        searchParams.put("LIKE_appCode", "%" + appCode + "%");
        Page<ManagerApp> page = managerAppDao.query(searchParams, new PageRequest(pageNo - 1, pageSize));
        AbstractQueryResult<ManagerAppInfo> result = new AbstractQueryResult<ManagerAppInfo>() {
        };
        result.setStatus(Status.SUCCESS);
        result.setCode(CommonResultCode.SUCCESS.getCode());
        result.setMessage(CommonResultCode.SUCCESS.getMessage());
        FacadeUtils.setQueryResult(result, new FacadeUtils.SpringDataPageExtractor<>(page));
        return result;
    }
}
