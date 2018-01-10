package org.smart4j.chapter2.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.model.Customer;

public class DatabaseHelperTest {

	@Before
	public void init() throws IOException {
		String filePath = "sql/insert.sql";
		DatabaseHelper.executeSqlFile(filePath);
	} 
	
	
	@Test
	public void testInsertEntity() {
		
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("name", "customer001");
		fieldMap.put("contact", "John");
		fieldMap.put("telephone", "15638737937");
		boolean result = DatabaseHelper.insertEntity(Customer.class, fieldMap);
		Assert.assertTrue(result);
	}

	@Test
	public void testUpdateEntity() {
		long id = 1;
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("contact", "Eric");
		boolean result = DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
		Assert.assertTrue(result);
	}

	@Test
	public void testDeleteEntity() {
		long id = 1;
		boolean result = DatabaseHelper.deleteEntity(Customer.class, id);
		Assert.assertTrue(result);
	}

}
