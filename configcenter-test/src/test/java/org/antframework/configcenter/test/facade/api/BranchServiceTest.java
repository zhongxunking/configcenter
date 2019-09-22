/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-18 23:00 创建
 */
package org.antframework.configcenter.test.facade.api;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.ComputeBranchMergenceResult;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.FindBranchesResult;
import org.antframework.configcenter.facade.vo.BranchConstants;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.facade.vo.ReleaseConstant;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 分支服务单元测试
 */
@Ignore
public class BranchServiceTest extends AbstractTest {
    private static final String[] BRANCH_IDS = {BranchConstants.DEFAULT_BRANCH_ID, "branchA"};

    @Autowired
    private BranchService branchService;

    @Test
    public void testAddBranch() {
        for (String branchId : BRANCH_IDS) {
            AddBranchOrder order = new AddBranchOrder();
            order.setAppId("customer");
            order.setProfileId("dev");
            order.setBranchId(branchId);
            order.setReleaseVersion(0L);

            EmptyResult result = branchService.addBranch(order);
            FacadeUtils.assertSuccess(result);
        }
    }

    @Test
    public void testReleaseBranch() {
        for (String branchId : BRANCH_IDS) {
            Set<Property> addOrModifiedProperties = new HashSet<>();
            addOrModifiedProperties.add(new Property("redis.host", "localhost", Scope.PRIVATE));
            addOrModifiedProperties.add(new Property("redis.port", "6379", Scope.PRIVATE));

            Set<String> removedPropertyKeys = new HashSet<>();
            removedPropertyKeys.add("zookeeper.url");

            ReleaseBranchOrder order = new ReleaseBranchOrder();
            order.setAppId("customer");
            order.setProfileId("dev");
            order.setBranchId(branchId);
            order.setAddOrModifiedProperties(addOrModifiedProperties);
            order.setRemovedPropertyKeys(removedPropertyKeys);
            order.setMemo("发布");

            ReleaseBranchResult result = branchService.releaseBranch(order);
            FacadeUtils.assertSuccess(result);
        }
    }

    @Test
    public void testRevertBranch() {
        RevertBranchOrder order = new RevertBranchOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);
        order.setTargetReleaseVersion(ReleaseConstant.ORIGIN_VERSION);

        EmptyResult result = branchService.revertBranch(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testMergeBranch() {
        MergeBranchOrder order = new MergeBranchOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);
        order.setSourceBranchId("branchA");

        EmptyResult result = branchService.mergeBranch(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testComputeBranchMergence() {
        ComputeBranchMergenceOrder order = new ComputeBranchMergenceOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);
        order.setSourceBranchId("branchA");

        ComputeBranchMergenceResult result = branchService.computeBranchMergence(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testDeleteBranch() {
        DeleteBranchOrder order = new DeleteBranchOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId("branchA");

        EmptyResult result = branchService.deleteBranch(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testFindBranch() {
        FindBranchOrder order = new FindBranchOrder();
        order.setAppId("customer");
        order.setProfileId("dev");
        order.setBranchId(BranchConstants.DEFAULT_BRANCH_ID);

        FindBranchResult result = branchService.findBranch(order);
        FacadeUtils.assertSuccess(result);
    }

    @Test
    public void testFindBranches() {
        FindBranchesOrder order = new FindBranchesOrder();
        order.setAppId("customer");
        order.setProfileId("dev");

        FindBranchesResult result = branchService.findBranches(order);
        FacadeUtils.assertSuccess(result);
    }
}
