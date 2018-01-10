package org.smart4j.framework.util;
/**
 * 配置文件工具类
 * 
 * @author Administrator
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

	/**
	 * 加载属性文件
	 */

	public static Properties loadProps(String fileName) {
		Properties properties = null;
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (is == null) {
				throw new FileNotFoundException(fileName + " file is not found!");
			}
			properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			logger.error("load properties file failure!", e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("close inputStream failure!", e);
				}
			}
		}

		return properties;
	}
	
	/**
	 * 获取字符型属性（默认为空字符串）
	 */
	public String getString(Properties props, String key) {
		return getString(props,key,"");
	}
	
	/**
	 * 获取字符型属性（可以指定默认值）
	 */
	public String getString(Properties props, String key,String deaultValue) {
		String value = deaultValue;
		if(props.containsKey(key)) {
			value = props.getProperty(key);
		}
		return value;
	}
	/**
	 * 获取数值型属性（默认值为0）
	 */
	public int getInt(Properties props, String key) {
		return getInt(props, key, 0);
	}
	
	/**
	 * 获取数值型属性（可以指定默认值）
	 */
	public int getInt(Properties props, String key,int deaultValue) {
		int value = deaultValue;
		if(props.containsKey(key)) {
			//value = CastUtil.castInt(props.getProperty(key));
			value = Integer.valueOf(props.getProperty(key));
		}
		return value;
	}
	/**
	 * 获取布尔型属性（默认值为false）
	 */
	public boolean getBoolean(Properties props, String key) {
		return getBoolean(props, key, false);
	}

	public boolean getBoolean(Properties props, String key, boolean deaultValue) {
		boolean value = deaultValue;
		if(props.containsKey(key)) {
			//value = CastUtil.castInt(props.getProperty(key));
			value = Boolean.valueOf(props.getProperty(key));
		}
		return value;
	}
	
	
}
