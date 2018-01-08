package org.smart4j.chapter2.service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.smart4j.chapter2.model.Customer;
/**
 * CustomerService 单元测试
 * 
 * @author Administrator
 *
 */
public class CustomerServiceTest {

	private final CustomerService customerService;

	public CustomerServiceTest(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@Test
	public void init() {
		// TODO 初始化数据库
	}
	
	@Test
	public void testGetCustomerList() {
		String keyword = null;
		List<Customer> customerList = customerService.getCustomerList(keyword);
		Assert.assertEquals(2, customerList.size());
		
	}
	
	@Test
	public void testGetCustomer() {
		long id = 1;
		Customer customer = customerService.getCustomer(id);
		Assert.assertNotNull(customer);
		
	}
	
	@Test
	public void testCreateCustomer() {
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("name", "customer001");
		fieldMap.put("contact", "John");
		fieldMap.put("telephone", "15638737937");
		boolean result = customerService.createCustomer(fieldMap);
		Assert.assertTrue(result);
	}
	@Test
	public void testUpdateCustomer() {
		long id = 1;
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("contact", "Eric");
		boolean result = customerService.updateCustomer(id, fieldMap);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testDeleteCustomer() {
		long id = 1;
		boolean result = customerService.deleteCustomer(id);
		Assert.assertTrue(result);
	}
}
