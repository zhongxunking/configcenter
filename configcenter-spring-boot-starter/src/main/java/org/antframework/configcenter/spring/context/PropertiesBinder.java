/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-21 12:13 创建
 */
package org.antframework.configcenter.spring.context;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

/**
 * 属性绑定器
 */
@AllArgsConstructor
public class PropertiesBinder {
    // 校验器
    private static final Validator VALIDATOR = new OptionalValidatorFactoryBean() {{
        afterPropertiesSet();
    }};
    // 转换器
    private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();

    // 属性资源
    private final PropertySources propertySources;

    /**
     * 构建属性对象
     *
     * @param targetClass 目标类型（必须有默认构造函数。如果被@ConfigurationProperties标注，则以注解为准进行属性绑定；否则按照属性前缀为null进行处理）
     * @return 绑定属性后的对象
     */
    public <T> T build(Class<T> targetClass) {
        T target = (T) ReflectUtils.newInstance(targetClass);
        ConfigurationProperties annotation = AnnotatedElementUtils.findMergedAnnotation(targetClass, ConfigurationProperties.class);
        if (annotation != null) {
            bind(target, annotation.prefix(), annotation.ignoreInvalidFields(), annotation.ignoreNestedProperties(), annotation.ignoreUnknownFields());
        } else {
            bind(target, null);
        }
        return target;
    }

    /**
     * 绑定属性
     *
     * @param target 目标对象
     * @param prefix 属性前缀
     */
    public void bind(Object target, String prefix) {
        bind(target, prefix, false, false, true);
    }

    /**
     * 绑定属性
     *
     * @param target                 目标对象
     * @param prefix                 属性前缀
     * @param ignoreInvalidFields    是否忽略类型不匹配的字段
     * @param ignoreNestedProperties 是否忽略内嵌型的属性
     * @param ignoreUnknownFields    是否忽略未知字段
     */
    public void bind(Object target, String prefix, boolean ignoreInvalidFields, boolean ignoreNestedProperties, boolean ignoreUnknownFields) {
        PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory(target);
        factory.setPropertySources(propertySources);
        factory.setValidator(VALIDATOR);
        factory.setConversionService(CONVERSION_SERVICE);
        factory.setTargetName(prefix);
        factory.setIgnoreInvalidFields(ignoreInvalidFields);
        factory.setIgnoreNestedProperties(ignoreNestedProperties);
        factory.setIgnoreUnknownFields(ignoreUnknownFields);

        try {
            factory.bindPropertiesToTarget();
        } catch (BindException e) {
            ExceptionUtils.rethrow(e);
        }
    }
}
