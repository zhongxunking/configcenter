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
        zkOperations.createNode("/dev/scbfund1/aa");
        zkOperations.createNode("/dev/scbfund1/bb");
        zkOperations.createNode("/dev/scbfund1/cc");
        zkOperations.createNode("/dev/scbfund1/dd");
        zkOperations.createNode("/dev/scbfund2");
        zkOperations.createNode("/dev/scbfund2/aa");
        zkOperations.createNode("/dev/scbfund2/bb");
        zkOperations.createNode("/dev/scbfund2/cc");
        zkOperations.createNode("/dev/scbfund2/dd");
        zkOperations.createNode("/dev/scbfund3");
        zkOperations.createNode("/dev/scbfund4");
        zkOperations.createNode("/dev/scbfund5");
    }

    @Test
    public void testDeleteNode(){
        zkOperations.deleteNode("/dev");
    }

    @Test
    public void testSetData() {
        zkOperations.setData("/dev/scbfund", null);
    }
}
