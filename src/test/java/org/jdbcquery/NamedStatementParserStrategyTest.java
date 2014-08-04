package org.jdbcquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdbcquery.NamedStatementParserStrategy;
import org.jdbcquery.ParsedNamedStatement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the sql statement parser.
 *
 * @author Troy Histed
 */
public class NamedStatementParserStrategyTest {

	private final NamedStatementParserStrategy parser = new NamedStatementParserStrategy();
	private String statement;
	private List<String> params;

	@Before
	public void constructStatement() {
		this.statement = null;
		this.params = null;
	}

	public void prepareStatement(String statement) {
		final ParsedNamedStatement parsedStatement = this.parser.prepareNamedStatement(statement);
		this.statement = parsedStatement.getStatement();
		this.params = parsedStatement.getParameters();
	}

	public void testPreparedStatement(String preparedStatement, List<String> preparedParams) {
		Assert.assertEquals(preparedStatement, this.statement.trim());
		Assert.assertEquals(preparedParams, this.params);
	}

	@Test
	public void testBasicStatement() {
		this.prepareStatement("Select something from something");
		this.testPreparedStatement("Select something from something", new ArrayList<String>());
	}

	@Test
	public void testNamedParameter() {
		this.prepareStatement("Select :param1 from something");
		this.testPreparedStatement("Select ? from something", Arrays.asList("param1"));
	}

	@Test
	public void testNamedParameterAtStart() {
		this.prepareStatement(":param1 Select foo from bar");
		this.testPreparedStatement("? Select foo from bar", Arrays.asList("param1"));
	}

	@Test
	public void testNamedParameterAtEnd() {
		this.prepareStatement("Select foo from bar :param1");
		this.testPreparedStatement("Select foo from bar ?", Arrays.asList("param1"));
	}

	@Test
	public void testNamedParameterInParens() {
		this.prepareStatement("Select foo(:param1) from bar");
		this.testPreparedStatement("Select foo(?) from bar", Arrays.asList("param1"));
	}

	@Test
	public void testNamedParameterInDoubleQuotes() {
		this.prepareStatement("Select foo\":param1\" from bar");
		this.testPreparedStatement("Select foo\":param1\" from bar", new ArrayList<String>());
	}

	@Test
	public void testNamedParameterInSingleQuotes() {
		this.prepareStatement("Select foo':param1' from bar");
		this.testPreparedStatement("Select foo':param1' from bar", new ArrayList<String>());
	}

	@Test
	public void testNamedParameterEndingInComma() {
		this.prepareStatement("Select foo :param1, from bar");
		this.testPreparedStatement("Select foo ?, from bar", Arrays.asList("param1"));
	}

	@Test
	public void testNamedParameterEndingInRightParen() {
		this.prepareStatement("Select foo :param1) from bar");
		this.testPreparedStatement("Select foo ?) from bar", Arrays.asList("param1"));
	}

	@Test
	public void testTwoNamedParameters() {
		this.prepareStatement("Select foo :param1 :param2 from bar");
		this.testPreparedStatement("Select foo ? ? from bar", Arrays.asList("param1", "param2"));
	}

	@Test
	public void testDuplicateNamedParameters() {
		this.prepareStatement("Select foo :param1 :param1 from bar");
		this.testPreparedStatement("Select foo ? ? from bar", Arrays.asList("param1", "param1"));
	}

}
