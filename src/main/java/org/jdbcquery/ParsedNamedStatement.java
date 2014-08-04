/**
 * Copyright 2014 Troy Histed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcquery;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ParsedNamedStatement [statement=" + this.statement + ", parameters=" + this.parameters + "]";
	}
}
