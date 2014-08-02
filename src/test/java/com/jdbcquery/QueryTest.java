package com.jdbcquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class QueryTest {

	private Query<Object> queryObject;
	private String query;
	private List<String> params;
	
	@Before
	public void constructQuery() {		
		this.queryObject = new Query<Object>();
		this.query = null;
		this.params = null;
	}
	
	public void prepareQuery(String query) {
		this.query = this.queryObject.prepareNamedStatement(query);
		this.params = this.queryObject.parameters;
	}
	
	public void testPreparedQuery(String preparedQuery, List<String> preparedParams) {
		Assert.assertEquals(preparedQuery, this.query.trim());
		Assert.assertEquals(preparedParams, this.params);
	}
	
	@Test
	public void testBasicQuery() {
		this.prepareQuery("Select something from something");
		this.testPreparedQuery("Select something from something", new ArrayList<String>());
	}
	
	@Test
	public void testNamedParameter() {
		this.prepareQuery("Select :param1 from something");
		this.testPreparedQuery("Select ? from something", Arrays.asList("param1"));
	}
	
	@Test
	public void testNamedParameterAtStart() {
		this.prepareQuery(":param1 Select foo from bar");
		this.testPreparedQuery("? Select foo from bar", Arrays.asList("param1"));
	}
	
	@Test
	public void testNamedParameterAtEnd() {
		this.prepareQuery("Select foo from bar :param1");
		this.testPreparedQuery("Select foo from bar ?", Arrays.asList("param1"));
	}
	
	@Test
	public void testNamedParameterInParens() {
		this.prepareQuery("Select foo(:param1) from bar");
		this.testPreparedQuery("Select foo(?) from bar", Arrays.asList("param1"));
	}
	
	@Test
	public void testNamedParameterInDoubleQuotes() {
		this.prepareQuery("Select foo\":param1\" from bar");
		this.testPreparedQuery("Select foo\":param1\" from bar", new ArrayList<String>());
	}
	
	@Test
	public void testNamedParameterInSingleQuotes() {
		this.prepareQuery("Select foo':param1' from bar");
		this.testPreparedQuery("Select foo':param1' from bar", new ArrayList<String>());
	}
	
	@Test
	public void testNamedParameterEndingInComma() {
		this.prepareQuery("Select foo :param1, from bar");
		this.testPreparedQuery("Select foo ?, from bar", Arrays.asList("param1"));
	}
	
	@Test
	public void testNamedParameterEndingInRightParen() {
		this.prepareQuery("Select foo :param1) from bar");
		this.testPreparedQuery("Select foo ?) from bar", Arrays.asList("param1"));
	}
	
	@Test
	public void testTwoNamedParameters() {
		this.prepareQuery("Select foo :param1 :param2 from bar");
		this.testPreparedQuery("Select foo ? ? from bar", Arrays.asList("param1", "param2"));
	}
	
	@Test
	public void testDuplicateNamedParameters() {
		this.prepareQuery("Select foo :param1 :param1 from bar");
		this.testPreparedQuery("Select foo ? ? from bar", Arrays.asList("param1", "param1"));
	}

}
