/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-12 21:42 创建
 */
package org.antframework.configcenter.client.support;

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
                throw new RuntimeException("不存在缓存文件：" + cacheFile.getPath());
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
            createFileIfAbsent(this.cacheFile);
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

    // 如果文件不存在，则创建文件
    private static void createFileIfAbsent(File file) {
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
            if (!parent.exists()) {
                throw new IllegalStateException("无权限创建文件夹：" + parent.getPath());
            }
        }
        try {
            file.createNewFile();
        } catch (Throwable e) {
            throw new IllegalStateException(String.format("创建文件[%s]失败：%s", file.getPath(), e.getMessage()), e);
        }
        if (!file.exists()) {
            throw new IllegalStateException("无权限创建文件：" + file.getPath());
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
