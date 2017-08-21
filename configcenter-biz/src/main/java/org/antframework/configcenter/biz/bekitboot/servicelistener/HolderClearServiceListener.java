/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 14:59 创建
 */
package org.antframework.configcenter.biz.bekitboot.servicelistener;

import org.antframework.configcenter.biz.bekitboot.holder.CodeMessageHolder;
import org.bekit.event.annotation.listener.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceFinishEvent;

/**
 * 持有器清理-服务监听器
 */
@ServiceListener(priority = 1)
public class HolderClearServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        // 清理结果码、结果描述持有器
        CodeMessageHolder.remove();
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        // 清理结果码、结果描述持有器
        CodeMessageHolder.remove();
    }
}
