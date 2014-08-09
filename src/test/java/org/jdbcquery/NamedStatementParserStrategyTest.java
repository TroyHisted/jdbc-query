package org.jdbcquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	/**
	 * Reset the tested instance members to null;
	 */
	@Before
	public void constructStatement() {
		this.statement = null;
		this.params = null;
	}

	/**
	 * Prepares the statement and assigns the parsed statement members to the instance members.
	 *
	 * @param statement the statement to prepare
	 */
	public void prepareStatement(String statement) {
		final ParsedNamedStatement parsedStatement = this.parser.prepareNamedStatement(statement);
		this.statement = parsedStatement.getStatement();
		this.params = parsedStatement.getParameters();
	}

	/**
	 * Verifies that the instance members match the passed in expected values.
	 *
	 * @param preparedStatement the expected statement
	 * @param preparedParams the expected parameter list
	 */
	public void testPreparedStatement(String preparedStatement, List<String> preparedParams) {
		Assert.assertEquals(preparedStatement, this.statement.trim());
		Assert.assertEquals(preparedParams, this.params);
	}

	/**
	 * Verify that a statement without named parameters results in the same statement and no parameters.
	 */
	@Test
	public void testBasicStatement() {
		this.prepareStatement("Select something from something");
		this.testPreparedStatement("Select something from something", new ArrayList<String>());
	}

	/**
	 * Verify that a statement with a single parameter results in a modified statement and a parameter array
	 * containing the name of the parameter.
	 */
	@Test
	public void testNamedParameter() {
		this.prepareStatement("Select :param1 from something");
		this.testPreparedStatement("Select ? from something", Arrays.asList("param1"));
	}

	/**
	 * Verify that a named parameter is located even when it's at the beginning of the statement.
	 */
	@Test
	public void testNamedParameterAtStart() {
		this.prepareStatement(":param1 Select foo from bar");
		this.testPreparedStatement("? Select foo from bar", Arrays.asList("param1"));
	}

	/**
	 * Verify that a named parameter is located even when it's at the end of the statement.
	 */
	@Test
	public void testNamedParameterAtEnd() {
		this.prepareStatement("Select foo from bar :param1");
		this.testPreparedStatement("Select foo from bar ?", Arrays.asList("param1"));
	}

	/**
	 * Verify that a named parameter is located even when it's inside of parenthesis.
	 */
	@Test
	public void testNamedParameterInParens() {
		this.prepareStatement("Select foo(:param1) from bar");
		this.testPreparedStatement("Select foo(?) from bar", Arrays.asList("param1"));
	}

	/**
	 * Verify that when the colon syntax is used within quotes, even though it may appear to be a named parameter
	 * it is not and should not be treated as one.
	 */
	@Test
	public void testNamedParameterInDoubleQuotes() {
		this.prepareStatement("Select foo\":param1\" from bar");
		this.testPreparedStatement("Select foo\":param1\" from bar", new ArrayList<String>());
	}

	/**
	 * Verify that when the colon syntax is used within single quotes, even though it may appear to be a named
	 * parameter it is not and should not be treated as one.
	 */
	@Test
	public void testNamedParameterInSingleQuotes() {
		this.prepareStatement("Select foo':param1' from bar");
		this.testPreparedStatement("Select foo':param1' from bar", new ArrayList<String>());
	}

	/**
	 * Verify that when a named parameter is terminated by a comma it is still located.
	 */
	@Test
	public void testNamedParameterEndingInComma() {
		this.prepareStatement("Select foo :param1, from bar");
		this.testPreparedStatement("Select foo ?, from bar", Arrays.asList("param1"));
	}

	/**
	 * Verify that when a named parameter is terminated by a right paren it is still located.
	 */
	@Test
	public void testNamedParameterEndingInRightParen() {
		this.prepareStatement("Select foo :param1) from bar");
		this.testPreparedStatement("Select foo ?) from bar", Arrays.asList("param1"));
	}

	/**
	 * Verify that multiple named parameters can be located.
	 */
	@Test
	public void testTwoNamedParameters() {
		this.prepareStatement("Select foo :param1 :param2 from bar");
		this.testPreparedStatement("Select foo ? ? from bar", Arrays.asList("param1", "param2"));
	}

	/**
	 * Verify multiple uses of the same parameter name result in multiple parameters in the list.
	 */
	@Test
	public void testDuplicateNamedParameters() {
		this.prepareStatement("Select foo :param1 :param1 from bar");
		this.testPreparedStatement("Select foo ? ? from bar", Arrays.asList("param1", "param1"));
	}

	/**
	 * Verify that parameters in comments are ignored in the prepared statement.
	 */
	@Test
	public void testCommentsRemoved() {
		this.prepareStatement("Select foo /*:fakeParam*/ from bar");
		this.testPreparedStatement("Select foo /*:fakeParam*/ from bar", new ArrayList<String>());
	}

	/**
	 * Verify that slashes around comments are persisted.
	 */
	@Test
	public void testExtraSlashes() {
		this.prepareStatement("Select foo //**// from bar");
		this.testPreparedStatement("Select foo //**// from bar", new ArrayList<String>());
	}
}
