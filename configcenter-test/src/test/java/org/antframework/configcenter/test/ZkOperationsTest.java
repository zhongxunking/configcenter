/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-07 19:45 创建
 */
package org.antframework.configcenter.test;

import org.antframework.configcenter.biz.ZkOperations;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class ZkOperationsTest extends AbstractTest {

    @Autowired
    private ZkOperations zkOperations;

    @Test
    public void testCreateNodesByPath() throws Exception {
        zkOperations.createNodesByPath("/dev/scbfund");
    }

    @Test
    public void testSetData() {
        zkOperations.setData("/dev/scbfund", null);
    }
}
