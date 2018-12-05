/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-22 21:52 创建
 */
package org.antframework.configcenter.facade.result;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.AppTree;

/**
 * 查找应用树result
 */
@Getter
@Setter
public class FindAppTreeResult extends AbstractResult {
    // 应用树
    private AppTree appTree;
}
