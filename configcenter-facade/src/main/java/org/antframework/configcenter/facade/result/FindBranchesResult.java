/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-13 15:31 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.BranchInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找应用在环境下的所有分支result
 */
@Getter
public class FindBranchesResult extends AbstractResult {
    // 应用在环境下的所有分支
    private final List<BranchInfo> branches = new ArrayList<>();

    public void addBranch(BranchInfo branch) {
        branches.add(branch);
    }
}
