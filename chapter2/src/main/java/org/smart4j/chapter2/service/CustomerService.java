package org.smart4j.chapter2.service;

import java.util.List;
import java.util.Map;

import org.smart4j.chapter2.model.Customer;
/**
 * 提供用户服务
 * @author Administrator
 *
 */
public interface CustomerService {

	/**
	 * 获取客户列表
	 * @param keyword
	 * @return
	 */
	List<Customer> getCustomerList(String keyword);
	/**
	 * 获取客户
	 * @param id
	 * @return
	 */
	Customer getCustomer(long id);
	/**
	 * 创建客户
	 * @param fieldMap
	 * @return
	 */
	boolean createCustomer(Map<String,Object> fieldMap);
	/**
	 * 更新客户
	 * @param id
	 * @param fieldMap
	 * @return
	 */
	boolean updateCustomer(long id, Map<String,Object> fieldMap);
	/**
	 * 删除客户
	 * @param id
	 * @return
	 */
	boolean deleteCustomer(long id);
}
