package org.smart4j.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;

/**
 * 类操作 工具类
 *
 * @author Administrator
 *
 * @since 1.0.0
 */
public final class ClassUtil {

	private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * 获取类加载器
	 * 
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 加载类(为了提高性能 请将 isInitialized 设置为 false)
	 * 
	 * @param className
	 * @param isInitialized
	 * @return
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> cls;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			logger.error("load class failure!", e);
			throw new RuntimeException(e);
		}
		return cls;
	}

	/**
	 * 获取指定包下所有类
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String packageName) {

		Set<Class<?>> classSet = new HashSet<>();

		Enumeration<URL> urls;
		try {
			urls = getClassLoader().getResources(packageName.replace(".", "/"));

			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					if ("file".equals(protocol)) {
						String packagePath = url.getPath().replaceAll("%20", "");
						addClass(classSet, packagePath, packageName);
					} else if ("jar".equals(protocol)) {
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if (jarURLConnection != null) {
							JarFile jarFile = jarURLConnection.getJarFile();
							if (jarFile != null) {
								Enumeration<JarEntry> JarEntries = jarFile.entries();
								while (JarEntries.hasMoreElements()) {
									JarEntry jarEntry = JarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if (jarEntryName.endsWith(".class")) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replaceAll("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("get class set failure!", e);
			throw new RuntimeException(e);
		}
		return classSet;
	}

	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
	/**
	 * 使用递归遍历所有的类型加入classSet容器
	 * @param classSet
	 * @param packagePath
	 * @param packageName
	 */
	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
		File[] files = new File(packagePath)
				.listFiles((f) -> f.isFile() && f.getName().endsWith(".class") || f.isDirectory());
		for (File file : files) {
			String fileName = file.getName();
			if(file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if(StringUtils.isNotEmpty(fileName)) {
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			}else {
				String subPackagePath = fileName;
				if(StringUtils.isNotEmpty(packagePath)) {
					subPackagePath = packagePath + "/" + subPackagePath; 
				}
				String subPackageName = fileName;
				if(StringUtils.isNotEmpty(packageName)) {
					subPackageName = packageName + "." + subPackageName; 
				}
				addClass(classSet, subPackagePath, subPackageName);
			}
		}
	}

}
