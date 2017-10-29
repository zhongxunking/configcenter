/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-12 21:42 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.common.util.file.FileUtils;
import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.core.DefaultConfigProperties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 缓存文件处理器
 */
public class CacheFileHandler {
    // 缓存文件
    private File cacheFile;

    public CacheFileHandler(ConfigContext.InitParams initParams) {
        this.cacheFile = new File(initParams.getCacheFilePath());
    }

    /**
     * 读取配置
     */
    public Map<String, String> readConfig() {
        try {
            if (!cacheFile.exists()) {
                throw new IllegalStateException("不存在缓存文件：" + cacheFile.getPath());
            }
            InputStream in = null;
            try {
                in = new FileInputStream(cacheFile);
                Properties props = new Properties();
                props.load(in);
                return propsToMap(props);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    /**
     * 缓存配置
     */
    public void storeConfig(Map<String, String> properties) {
        try {
            // 如果缓存文件不存在，则创建
            FileUtils.createFileIfAbsent(cacheFile.getPath());
            OutputStream out = null;
            try {
                out = new FileOutputStream(cacheFile);
                mapToProps(properties).store(out, "updated at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            ExceptionUtils.rethrow(e);
        }
    }

    // Properties转Map
    private static Map<String, String> propsToMap(Properties props) {
        Map<String, String> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, DefaultConfigProperties.toRawValue(props.getProperty(key)));
        }
        return map;
    }

    // Map转Properties
    private static Properties mapToProps(Map<String, String> map) {
        Properties props = new Properties();
        for (String key : map.keySet()) {
            props.setProperty(key, DefaultConfigProperties.toSavableValue(map.get(key)));
        }
        return props;
    }
}
