package com.jdbcquery;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses an sql statement and replaces the named parameters with question marks and puts the named parameters
 * into a list in the order of appearance in the statement.
 *
 * @author Troy Histed
 */
public class NamedStatementParserStrategy {

	/**
	 * Given a query this will extract the named parameters and replace them with the prepared statement variable
	 * marker "?". The named parameters will be inserted into a list in the order of appearance in the original
	 * statement.
	 *
	 * @param aStatement
	 *            the statement to prepare for use as a named prepared statement
	 * @return the parsed named statement
	 */
	ParsedNamedStatement prepareNamedStatement(String aStatement) {

		final char[] statement = aStatement.toCharArray();
		final char[] parsedStatement = new char[statement.length];
		final List<String> parameters = new ArrayList<String>();

		int i = 0;
		int j = 0;
		while (i < statement.length) {
			if (statement[i] == '/') {
				if (i + 1 < statement.length && statement[i + 1] == '*') {
					parsedStatement[j++] = ' ';
					i++; // skip the /
					i++; // skip the *
					while (statement[i] != '/' || statement[i - 1] != '*') {
						i++; // skip the body of the comment
					}
					i++; // skip the closing /
				} else {
					parsedStatement[j++] = statement[i++]; // add the / since it's not a comment
				}
			} else if (statement[i] == '\'' && statement[i - 1] != '\\') {
				parsedStatement[j++] = statement[i++]; // Add the '
				while (statement[i] != '\'' || statement[i - 1] == '\\') {
					parsedStatement[j++] = statement[i++];
				}
				parsedStatement[j++] = statement[i++]; // add the final '
			} else if (statement[i] == '\"' && statement[i - 1] != '\\') {
				parsedStatement[j++] = statement[i++]; // Add the '
				while (statement[i] != '\"' || statement[i - 1] == '\\') {
					parsedStatement[j++] = statement[i++];
				}
				parsedStatement[j++] = statement[i++]; // add the final '
			} else if (statement[i] == ':') {
				parsedStatement[j++] = '?'; // replace with a prepared statement marker
				i++;
				int lengthOfParameterName = 0;
				while (i < statement.length
						&& statement[i] != ' ' && statement[i] != ')' && statement[i] != ',') {
					lengthOfParameterName++;
					i++;
				}
				final char[] parameterName = new char[lengthOfParameterName];
				for (int index = 1; index <= lengthOfParameterName; index++) {
					parameterName[lengthOfParameterName - index] = statement[i - index];
				}

				final String param = String.valueOf(parameterName);
				parameters.add(param);
			} else {
				parsedStatement[j++] = statement[i++];
			}
		}

		while (j < parsedStatement.length) {
			parsedStatement[j++] = ' ';
		}

		final ParsedNamedStatement parsedNamedStatement = new ParsedNamedStatement();
		parsedNamedStatement.setStatement(String.valueOf(parsedStatement));
		parsedNamedStatement.setParameters(parameters);
		return parsedNamedStatement;
	}
}
