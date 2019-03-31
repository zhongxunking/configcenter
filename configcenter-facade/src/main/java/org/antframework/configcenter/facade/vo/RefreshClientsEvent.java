/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-03-31 17:01 创建
 */
package org.antframework.configcenter.facade.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.tostring.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * 刷新客户端事件
 */
@AllArgsConstructor
@Getter
public final class RefreshClientsEvent implements Serializable {
    // 需要刷新的配置主题
    private final Set<ConfigTopic> topics;

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
