/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 16:40 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyOrder;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.ProfileService;
import org.antframework.configcenter.facade.order.AddOrModifyProfileOrder;
import org.antframework.configcenter.facade.order.DeleteProfileOrder;
import org.antframework.configcenter.facade.order.QueryProfilesOrder;
import org.antframework.configcenter.facade.result.FindAllProfilesResult;
import org.antframework.configcenter.facade.result.QueryProfilesResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 环境服务单元测试
 */
@Ignore
public class ProfileServiceTest extends AbstractTest {
    @Autowired
    private ProfileService profileService;

    @Test
    public void testAddOrModifyProfile() {
        AddOrModifyProfileOrder order = new AddOrModifyProfileOrder();
        order.setProfileId("dev");
        order.setProfileName("开发环境");

        EmptyResult result = profileService.addOrModifyProfile(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testDeleteProfile() {
        DeleteProfileOrder order = new DeleteProfileOrder();
        order.setProfileId("dev");

        EmptyResult result = profileService.deleteProfile(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAllProfiles() {
        FindAllProfilesResult result = profileService.findAllProfiles(new EmptyOrder());
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryProfiles() {
        QueryProfilesOrder order = new QueryProfilesOrder();
        order.setPageNo(1);
        order.setPageSize(10);

        QueryProfilesResult result = profileService.queryProfiles(order);
        checkResult(result, Status.SUCCESS);
    }
}
