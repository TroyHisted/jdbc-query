package org.jdbcquery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the Statement Class.
 *
 * @author Troy Histed
 */
public class StatementTest {

	MockStatement statement;
	Map<Integer,Object> expected;

	/**
	 * Resets the instance members to new instances.
	 */
	@Before
	public void constructStatement() {
		this.statement = new MockStatement();
		this.expected = new HashMap<Integer, Object>();
	}

	/**
	 * Tests the set method for strings.
	 */
	@Test
	public void testSetString() {
		this.statement.setParameters(Arrays.asList("param1", "param2", "param1", "param3"));
		this.statement.set("param1", "foo");
		this.statement.set("param2", "bar");
		this.statement.set("param3", (String) null);

		this.expected.put(Integer.valueOf(1), "foo");
		this.expected.put(Integer.valueOf(2), "bar");
		this.expected.put(Integer.valueOf(3), "foo");
		this.expected.put(Integer.valueOf(4), null);

		Assert.assertEquals(this.expected, this.statement.getPreparedStatement().getValues());
	}

	/**
	 * Tests the set method for null Strings.
	 */
	@Test
	public void testSetNullString() {
		this.statement.setParameters(Arrays.asList("param1", "param2", "param1"));
		this.statement.setNull("param1", java.sql.Types.INTEGER);
		this.statement.setNull("param2", java.sql.Types.VARCHAR);

		this.expected.put(Integer.valueOf(1), null);
		this.expected.put(Integer.valueOf(2), null);
		this.expected.put(Integer.valueOf(3), null);

		Assert.assertEquals(this.expected, this.statement.getPreparedStatement().getValues());
	}

	/**
	 * Test the setBean method.
	 */
	@Test
	public void testSetBean() {

		final TestBean testBean = new TestBean();
		testBean.setParam1("value1");
		testBean.setParam2(Integer.valueOf(2));
		testBean.setParam3(3L);
		final TestBean nestedBean = new TestBean();
		nestedBean.setParam1("nestedValue1");
		testBean.setBean(nestedBean);

		this.statement.setParameters(Arrays.asList("param1", "param2", "param1", "param3", "bean.param1"));

		this.statement.setBean(testBean);

		this.expected.put(Integer.valueOf(1), "value1");
		this.expected.put(Integer.valueOf(2), Integer.valueOf(2));
		this.expected.put(Integer.valueOf(3), "value1");
		this.expected.put(Integer.valueOf(4), Long.valueOf(3L));
		this.expected.put(Integer.valueOf(5), "nestedValue1");

		Assert.assertEquals(this.expected, this.statement.getPreparedStatement().getValues());
	}
}
