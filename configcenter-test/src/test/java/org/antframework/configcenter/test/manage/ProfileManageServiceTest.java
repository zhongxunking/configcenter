/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 16:40 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.ProfileManageService;
import org.antframework.configcenter.facade.order.manage.AddOrModifyProfileOrder;
import org.antframework.configcenter.facade.order.manage.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.manage.FindAllProfileOrder;
import org.antframework.configcenter.facade.order.manage.QueryProfileOrder;
import org.antframework.configcenter.facade.result.manage.FindAllProfileResult;
import org.antframework.configcenter.facade.result.manage.QueryProfileResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class ProfileManageServiceTest extends AbstractTest {
    @Autowired
    private ProfileManageService profileManageService;

    @Test
    public void testAddOrModifyProfile() {
        AddOrModifyProfileOrder order = new AddOrModifyProfileOrder();
        order.setProfileCode("dev");
        order.setMemo("开发环境");
        EmptyResult result = profileManageService.addOrModifyProfile(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeleteProfile() {
        DeleteProfileOrder order = new DeleteProfileOrder();
        order.setProfileCode("dev");
        EmptyResult result = profileManageService.deleteProfile(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAllProfile() {
        FindAllProfileResult result = profileManageService.findAllProfile(new FindAllProfileOrder());
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryProfile() {
        QueryProfileOrder order = new QueryProfileOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        order.setProfileCode("dev");
        QueryProfileResult result = profileManageService.queryProfile(order);
        checkResult(result, Status.SUCCESS);
    }
}
