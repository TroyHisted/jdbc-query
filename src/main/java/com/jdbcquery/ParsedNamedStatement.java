package com.jdbcquery;

import java.util.List;

/**
 * Represents an SQL statement that has been parsed into a statement without named parameters and a list of those
 * named parameters.
 *
 * @author Troy Histed
 */
public class ParsedNamedStatement {

	private String statement;
	private List<String> parameters;

	/**
	 * @return the statement
	 */
	public String getStatement() {
		return this.statement;
	}
	/**
	 * @param statement the statement to set
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	/**
	 * @return the parameters
	 */
	public List<String> getParameters() {
		return this.parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParsedNamedStatement [statement=" + this.statement + ", parameters=" + this.parameters + "]";
	}
}
