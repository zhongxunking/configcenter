/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-12-21 17:13 创建
 */
package org.antframework.configcenter.biz.util;

import org.antframework.boot.bekit.CommonQueryConstant;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询工具类
 */
public class QueryUtils {
    /**
     * 查询排序
     */
    public static final Sort QUERY_SORT = new Sort(Sort.Direction.DESC, "id");

    /**
     * 构建查询附件
     *
     * @param daoClass dao类型
     * @return 附件
     */
    public static Map<Object, Object> buildQueryAttachment(Class daoClass) {
        Map<Object, Object> attachment = new HashMap<>();
        attachment.put(CommonQueryConstant.DAO_CLASS_KEY, daoClass);
        attachment.put(CommonQueryConstant.SORT_KEY, QUERY_SORT);

        return attachment;
    }
}
