/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-12 21:42 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.configcenter.client.ConfigContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class CacheFileHandler {
    private static final Logger logger = LoggerFactory.getLogger(CacheFileHandler.class);

    private File cacheFile;

    public CacheFileHandler(ConfigContext.ConfigParams configParams) {
        this.cacheFile = new File(configParams.getCacheFilePath());
        createFileIfAbsent(this.cacheFile);
    }

    public Map<String, String> readProperties() {
        try {
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
            return ExceptionUtils.wrapAndThrow(e);
        }
    }

    public void writeProperties(Map<String, String> properties) {
        try {
            OutputStream out = null;
            try {
                out = new FileOutputStream(cacheFile);
                mapToProps(properties).store(out, "configcenter-client create at " + new Date());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    private static void createFileIfAbsent(File file) {
        try {
            if (file.exists()) {
                return;
            }
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            file.createNewFile();
            if (!file.exists()) {
                throw new RuntimeException("创建文件失败：" + file.getPath());
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    private static Map<String, String> propsToMap(Properties props) {
        Map<String, String> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
        return map;
    }

    private static Properties mapToProps(Map<String, String> map) {
        Properties props = new Properties();
        for (String key : map.keySet()) {
            props.setProperty(key, map.get(key));
        }
        return props;
    }
}
