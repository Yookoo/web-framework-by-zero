package org.smart4j.framework.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.PropsUtil;

public class DatabaseHelper {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);
	/**
	 * apache 提供的用于SQL操作的对象;
	 */
	private static final QueryRunner queryRunner = new QueryRunner();
	/**
	 * 使用ThreadLocal保证Connection线程安全
	 */
	private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

	
	/**
	 * dbcp 连接池
	 */
	private static final BasicDataSource dataSource;
	
	private static final String FILE_NAME = "config.properties";
	private static final String DRIVER = "jdbc.driver";
	private static final String URL = "jdbc.url";
	private static final String USERNAME = "jdbc.username";
	private static final String PASSWORD = "jdbc.password";

	/**
	 * 使用静态代码块初始化常量
	 */

	static {
		
		Properties config = PropsUtil.loadProps(FILE_NAME);

		dataSource = new BasicDataSource();
		dataSource.setDriverClassName(config.getProperty(DRIVER));
		dataSource.setUrl(config.getProperty(URL));
		dataSource.setUsername(config.getProperty(USERNAME));
		dataSource.setPassword(config.getProperty(PASSWORD));
		
		
	}


	/**
	 * 查询实体列表
	 * 
	 */
	public static <T> List<T> queryEntryList(Class<T> entityClass, String sql, Object... params) {

		List<T> entityList = null;
		try {
			entityList = queryRunner.query(getConnection(), sql, new BeanListHandler<T>(entityClass), params);
		} catch (SQLException e) {
			logger.error("query entry list failure!", e);
			new RuntimeException(e);
		} 
		return entityList;
	}

	/**
	 * 查询实体类
	 * 
	 */
	public static <T> T queryEntry(Class<T> entityClass, String sql, Object... params) {

		T entity = null;
		try {
			entity = queryRunner.query(getConnection(), sql, new BeanHandler<T>(entityClass), params);
		} catch (SQLException e) {
			logger.error("query entry failure!", e);
			new RuntimeException(e);
		} 
		return entity;
	}

	/**
	 * 通用查询方法
	 * 
	 * @param sql
	 * @param params
	 * @return List<Map<String,Object>>
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object... params) {

		List<Map<String, Object>> result = null;
		try {
			result = queryRunner.query(getConnection(), sql, new MapListHandler(), params);
		} catch (SQLException e) {
			logger.error("execute query failure!", e);
			new RuntimeException(e);
		} 
		return result;
	}

	/**
	 * 通用更新方法（包括 update、insert、delete）
	 * 
	 * @return 返回修改的行数
	 */
	public static int executeUpdate(String sql, Object... params) {
		int rows = 0;
		try {
			rows = queryRunner.update(getConnection(), sql, params);
		} catch (SQLException e) {
			logger.error("execute update failure!", e);
			new RuntimeException(e);
		} 
		return rows;
	}

	/**
	 * 插入实体
	 * 拼写Sql语句处待优化。 TODO
	 * @return
	 */
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
		if (MapUtils.isEmpty(fieldMap)) {
			logger.error("can not insert entity: fieldMap is empty!");
			return false;
		}
		String sql = "INSERT INTO " + getTableName(entityClass);
		StringBuffer columns = new StringBuffer("(");
		StringBuffer values = new StringBuffer("(");

		for (String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append(",");
			values.append("?,");
		}
		columns.replace(columns.lastIndexOf(","), columns.length(), ")");
		values.replace(values.lastIndexOf(","), values.length(), ")");
		sql += columns + " VALUES " + values;

		Object[] params = fieldMap.values().toArray();
	
		logger.debug("insert sql : {}", sql);

		return executeUpdate(sql, params) == 1;
	}
	/**
	 * 修改实体
	 * 拼写Sql语句处待优化。 TODO
	 * @return
	 */
	public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
		if (MapUtils.isEmpty(fieldMap)) {
			logger.error("can not update entity: fieldMap is empty!");
			return false;
		}

		String sql = "UPDATE " + getTableName(entityClass) + " SET ";
		StringBuffer columns = new StringBuffer();
		for (String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append("=?, ");
		}
		sql += columns.substring(0, columns.lastIndexOf(",")) + " WHERE id=?";
		
		List<Object> paramList = new ArrayList<>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		Object[] params = paramList.toArray();
		
		logger.debug("update sql : ${}", sql);
		
		return executeUpdate(sql, params) == 1;
	}
	
	/**
	 * 删除实体
	 * 拼写Sql语句处待优化。 TODO
	 * @return
	 */
	public static <T> boolean deleteEntity(Class<T> entityClass, long id) {

		String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id=?";
		
		logger.debug("delete sql : {}", sql);
		return executeUpdate(sql, id) == 1;
	}
	/**
	 * 获取TableName的方法（默认tableName和类名一致，若不一致需要修改此方法。）
	 * @param entityClass
	 * @return
	 */
	private static <T> String getTableName(Class<T> entityClass) {
		return entityClass.getSimpleName();
	}

	/**
	 * 获取数据库连接
	 */

	public static Connection getConnection() {
		/**
		 * 从ThreadLocal中获取 connection
		 */
		Connection connection = connectionHolder.get();
		if (connection == null) {
			try {
				connection = dataSource.getConnection();
			} catch (SQLException e) {
				logger.error("get connection failure!", e);
				throw new RuntimeException(e);
			} finally {
				/**
				 * 将connection放入ThreadLocal
				 */
				connectionHolder.set(connection);
			}
		}
		return connection;
	}

	public static void executeSqlFile(String filePath){
		
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String sql ;
		try {
			while ((sql = reader.readLine())!= null) {
				DatabaseHelper.executeUpdate(sql);
			}
		} catch (IOException e) {
			logger.error("execute sql file failure!", e);
		}
	}
	
	/**
	 * 关闭数据库连接
	 * (使用dataSource连接池后，此方法弃用，并删除了所有finally中的closeConnection()方法)
	 * 
	 * @param connection
	 */
	@Deprecated
	public static void closeConnection() {
		/**
		 * 从ThreadLocal中获取 connection
		 */
		Connection connection = connectionHolder.get();
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("close connection failure!", e);
				throw new RuntimeException(e);
			} finally {
				connectionHolder.remove();
			}
		}
	}
	
	/**
	 * 使用连接池之后此方法弃用
	 */
	@Deprecated
	public static void loadJdbcDriver() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("load jdbc driver failure!", e);
		}
	}
}
