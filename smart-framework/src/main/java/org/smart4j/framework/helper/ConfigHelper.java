package org.smart4j.framework.helper;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.smart4j.framework.anontation.Controller;
import org.smart4j.framework.anontation.Service;
import org.smart4j.framework.constant.ConfigConstant;
import org.smart4j.framework.util.ClassUtil;
import org.smart4j.framework.util.PropsUtil;

/**
 * 
 * Config 助手类
 * 
 * @author Administrator
 *
 * @since 1.0.0
 */
public final class ConfigHelper {
	/**
	 * 定义集合类（用于存放所需的加载类）
	 */
	private static final Set<Class<?>> classSet;
	
	static {
		String basePackage = ConfigHelper.getAppBasePackage();
		classSet = ClassUtil.getClassSet(basePackage);
	}

	private static final Properties configProps = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
	
	/**
	 * 获取应用包名下的所有的类
	 */
	public static Set<Class<?>> getClassSet() {
		return classSet;
	}
	/**
	 * 获取应用包名下的所有Service的类
	 */
	public static Set<Class<?>> getServiceClassSet() {
		Set<Class<?>> classSet = new HashSet<>();
		for (Class<?> cls : classSet) {
			if (cls.isAnnotationPresent(Service.class)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
	/**
	 * 获取应用包名下的所有Controller的类
	 */
	public static Set<Class<?>> getControllerClassSet() {
		Set<Class<?>> classSet = new HashSet<>();
		for (Class<?> cls : classSet) {
			if (cls.isAnnotationPresent(Controller.class)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下的所有Bean的类(包括Controller和Service)
	 */
	public static Set<Class<?>> getBeanClassSet() {
		Set<Class<?>> beanClassSet = new HashSet<>();
		beanClassSet.addAll(getServiceClassSet());
		beanClassSet.addAll(getControllerClassSet());
		return beanClassSet;
	}
	
	/**
	 * 获取JDBC 驱动
	 */
	public static String getJdbcDriver() {
		return PropsUtil.getString(configProps, ConfigConstant.JDBC_DRIVER);
	}

	/**
	 * 获取JDBC URL
	 */
	public static String getJdbcUrl() {
		return PropsUtil.getString(configProps, ConfigConstant.JDBC_URL);
	}
	/**
	 * 获取JDBC username
	 */
	public static String getJdbcUsername() {
		return PropsUtil.getString(configProps, ConfigConstant.JDBC_USERNAME);
	}
	
	public static String getJdbcPassword() {
		return PropsUtil.getString(configProps, ConfigConstant.JDBC_PASSWORD);
	}
	public static String getAppBasePackage() {
		return PropsUtil.getString(configProps, ConfigConstant.APP_BASE_PACKAGE);
	}
	public static String getAppJspPath() {
		return PropsUtil.getString(configProps, ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
	}
	
	public static String getAppAssetPath() {
		return PropsUtil.getString(configProps, ConfigConstant.APP_ASSET_PATH, "/asset/");
	}
}

