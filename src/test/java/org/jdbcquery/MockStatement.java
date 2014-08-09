package org.jdbcquery;

import java.util.ArrayList;
import java.util.List;

/**
 * A mocked statement.
 *
 * @author Troy Histed
 */
public class MockStatement extends Statement {

	private List<String> parameters = new ArrayList<String>();
	private final MockPreparedStatement preparedStatement = new MockPreparedStatement();
	private final JdbcConnection jdbcConnection = new JdbcConnection(new MockConnection());

	void setParameters(List<String> aParameters) {
		this.parameters = aParameters;
	}

	@Override
	protected List<String> getParameters() {
		return this.parameters;
	}

	@Override
	protected JdbcConnection getConnection() {
		return this.jdbcConnection;
	}

	@Override
	protected MockPreparedStatement getPreparedStatement() {
		return this.preparedStatement;
	}

}
