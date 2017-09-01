/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:42 创建
 */
package org.antframework.configcenter.test;

import org.antframework.boot.core.Apps;
import org.antframework.configcenter.Main;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 单元测试父类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class AbstractTest {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractTest.class);

    static {
        // 设置使用环境
        Apps.setProfileIfNotExists("dev");
    }

}
