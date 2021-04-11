/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-25 23:44 创建
 */
package org.antframework.configcenter.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.common.util.tostring.ToString;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.manager.biz.util.Relations;
import org.antframework.manager.facade.info.RelationInfo;
import org.antframework.manager.web.CurrentManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 应用与配置类型规则的工具
 */
public final class AppPropertyTypes {
    // 关系类型（source=appId, target=keyRegex, value=PropertyType|priority）
    private static final String RELATION_TYPE = "app-propertyType";
    // 分隔符
    private static final char SEPARATOR = '|';
    // 掩码后的配置value
    private static final String MASKED_VALUE = "******";

    /**
     * 断言当前管理员是超级管理员或提供的配置key对应的value都是读写类型
     *
     * @param appId 应用id
     * @param keys  配置key
     */
    public static void assertAdminOrOnlyReadWrite(String appId, Set<String> keys) {
        try {
            CurrentManagerAssert.admin();
        } catch (BizException e) {
            List<AppRule> inheritedAppRules = findInheritedAppRules(appId);
            Set<String> notReadWriteKeys = new HashSet<>();
            for (String key : keys) {
                PropertyType type = computePropertyType(inheritedAppRules, key);
                if (type != PropertyType.READ_WRITE) {
                    notReadWriteKeys.add(key);
                }
            }

            if (!notReadWriteKeys.isEmpty()) {
                throw new BizException(Status.FAIL, CommonResultCode.UNAUTHORIZED.getCode(), String.format("存在敏感配置%s被修改", ToString.toString(notReadWriteKeys)));
            }
        }
    }

    /**
     * 添加或修改操作权限
     *
     * @param appId 应用id
     * @param rule  规则
     */
    public static void addOrModifyRule(String appId, Rule rule) {
        String value = rule.getPropertyType().name() + SEPARATOR + rule.getPriority();
        Relations.addOrModifyRelation(RELATION_TYPE, appId, rule.getKeyRegex(), value);
    }

    /**
     * 删除规则
     *
     * @param appId    应用id
     * @param keyRegex 规则的key正则表达式
     */
    public static void deleteRule(String appId, String keyRegex) {
        Relations.deleteRelations(RELATION_TYPE, appId, keyRegex);
    }

    /**
     * 删除所有规则
     *
     * @param appId 应用id
     */
    public static void deleteAllRule(String appId) {
        Relations.deleteRelations(RELATION_TYPE, appId, null);
    }

    /**
     * 对敏感配置掩码
     *
     * @param appId      应用id
     * @param properties 需掩码的配置集
     * @return 掩码后的配置集
     */
    public static Set<Property> maskProperties(String appId, Set<Property> properties) {
        List<AppRule> inheritedAppRules = findInheritedAppRules(appId);
        Set<Property> maskedProperties = new HashSet<>(properties.size());
        for (Property property : properties) {
            PropertyType type = computePropertyType(inheritedAppRules, property.getKey());
            if (type == PropertyType.NONE) {
                maskedProperties.add(new Property(property.getKey(), MASKED_VALUE, property.getScope()));
            } else {
                maskedProperties.add(property);
            }
        }
        return maskedProperties;
    }

    /**
     * 对敏感配置value掩码
     *
     * @param appId          应用id
     * @param propertyValues 需掩码的配置value集
     * @return 掩码后的配置value集
     */
    public static List<PropertyValueInfo> maskPropertyValues(String appId, List<PropertyValueInfo> propertyValues) {
        List<AppRule> inheritedAppRules = findInheritedAppRules(appId);
        List<PropertyValueInfo> maskedPropertyValues = new ArrayList<>(propertyValues.size());
        for (PropertyValueInfo propertyValue : propertyValues) {
            PropertyValueInfo maskedPropertyValue = new PropertyValueInfo();
            BeanUtils.copyProperties(propertyValue, maskedPropertyValue);
            PropertyType type = computePropertyType(inheritedAppRules, propertyValue.getKey());
            if (type == PropertyType.NONE) {
                maskedPropertyValue.setValue(MASKED_VALUE);
            }
            maskedPropertyValues.add(maskedPropertyValue);
        }

        return maskedPropertyValues;
    }

    // 计算配置类型
    private static PropertyType computePropertyType(List<AppRule> inheritedAppRules, String key) {
        for (AppRule appRule : inheritedAppRules) {
            for (Rule rule : appRule.getRules()) {
                if (Pattern.matches(rule.getKeyRegex(), key)) {
                    return rule.getPropertyType();
                }
            }
        }
        return PropertyType.READ_WRITE;
    }

    /**
     * 查找继承的应用规则
     *
     * @param appId 应用id
     * @return 由近及远继承的应用规则（该应用本身在第一位）
     */
    public static List<AppRule> findInheritedAppRules(String appId) {
        List<AppRule> appRules = new ArrayList<>();
        for (AppInfo app : Apps.findInheritedApps(appId)) {
            List<Rule> rules = new ArrayList<>();
            for (RelationInfo relation : Relations.findAllSourceRelations(RELATION_TYPE, app.getAppId())) {
                rules.add(parseToRule(relation));
            }
            rules.sort(Comparator.comparingInt(Rule::getPriority));

            AppRule appRule = new AppRule();
            appRule.setApp(app);
            appRule.setRules(rules);

            appRules.add(appRule);
        }
        return appRules;
    }

    private static Rule parseToRule(RelationInfo relation) {
        String[] parts = StringUtils.split(relation.getValue(), SEPARATOR);
        return new Rule(relation.getTarget(), PropertyType.valueOf(parts[0]), Integer.parseInt(parts[1]));
    }

    /**
     * 应用规则
     */
    @Getter
    @Setter
    public static class AppRule extends AbstractInfo {
        // 应用
        private AppInfo app;
        // 规则集（优先级由高到低）
        private List<Rule> rules;
    }

    /**
     * 规则
     */
    @AllArgsConstructor
    @Getter
    public static final class Rule implements Serializable {
        // key正则表达式
        private final String keyRegex;
        // 配置类型
        private final PropertyType propertyType;
        // 优先级
        private final int priority;

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }
}
