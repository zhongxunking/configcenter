/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-30 23:14 创建
 */
package org.antframework.configcenter.biz.converter;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Releases;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.springframework.core.convert.converter.Converter;

/**
 * 分支转换器
 */
public class BranchConverter implements Converter<Branch, BranchInfo> {
    // 转换器
    private static final Converter<Branch, BranchInfo> CONVERTER = new FacadeUtils.DefaultConverter<>(BranchInfo.class);

    @Override
    public BranchInfo convert(Branch source) {
        ReleaseInfo release = Releases.findRelease(source.getAppId(), source.getProfileId(), source.getReleaseVersion());
        if (release == null) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("分支[appId=%s,profileId=%s,branchId=%s]对应的发布版本[%d]不存在", source.getAppId(), source.getProfileId(), source.getBranchId(), source.getReleaseVersion()));
        }
        BranchInfo branch = CONVERTER.convert(source);
        branch.setRelease(release);
        return branch;
    }
}
