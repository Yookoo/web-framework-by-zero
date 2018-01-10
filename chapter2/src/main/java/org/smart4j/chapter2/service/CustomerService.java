package org.smart4j.chapter2.service;

import java.util.List;
import java.util.Map;

import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;

/**
 * 提供用户服务
 * 
 * @author Administrator
 *
 */
public class CustomerService {

	/**
	 * 获取客户列表
	 * 
	 * @param keyword
	 * @return
	 */
	public List<Customer> getCustomerList() {

		String sql = "SELECT * FROM customer";

		return DatabaseHelper.queryEntryList(Customer.class, sql);

	}

	/**
	 * 获取客户
	 * 
	 * @param id
	 * @return
	 */
	Customer getCustomer(long id) {
		String sql = "SELECT * FROM customer WHERE id = ?";
		return DatabaseHelper.queryEntry(Customer.class, sql, id);
	}

	/**
	 * 创建客户
	 * 
	 * @param fieldMap
	 * @return
	 */
	boolean createCustomer(Map<String, Object> fieldMap) {
		return DatabaseHelper.insertEntity(Customer.class, fieldMap);
	}

	/**
	 * 更新客户
	 * 
	 * @param id
	 * @param fieldMap
	 * @return
	 */
	boolean updateCustomer(long id, Map<String, Object> fieldMap) {
		return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
	}

	/**
	 * 删除客户
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteCustomer(long id) {
		return false;
	}
}
