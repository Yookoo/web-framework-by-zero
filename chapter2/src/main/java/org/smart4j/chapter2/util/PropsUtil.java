package org.smart4j.chapter2.util;
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

		return null;
	}
}
