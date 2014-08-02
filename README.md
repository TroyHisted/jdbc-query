jdbc-query
==========
[![Build Status](https://travis-ci.org/TroyHisted/jdbc-query.svg?branch=master)](https://travis-ci.org/TroyHisted/jdbc-query)

JDBC query builder supporting named parameters and facilitating a more concise java syntax.

## Features
* Named parameters
* Automatic housekeeping
* Removes boilerplate

## Sample

```java
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jdbcquery.Query;
import com.jdbcquery.RowMapper;

public class EmployeeDao {

	public String retrieveName(int employeeId) {
		return Query.forString("SELECT name FROM employees WHERE empId = :employeeId")
				.set("employeeId", employeeId)
				.execute();
	}
	
	public Date retrieveHireDate(int employeeId) {
		return Query.forObject(
				"SELECT hired FROM employees WHERE empId = :employeeId",
				new RowMapper<Date>(){
					protected Date mapRow(ResultSet resultSet) throws SQLException {
						return resultSet.getDate("hired");
				}})
			.set("employeeId", employeeId)
			.execute();
	}
}

```

## Configuration
* Create a class that implements _JdbcConnection_.
* Expose the class as an SPI service.
  * Create a file called _com.jdbcquery.JdbcConnector_ in your _META-INF_ folder.
  * In that file specify the fully qualified name of your _JdbcConnection_ implementation.
 
### Sample JdbcConnection implementation
```java
package com.mydomain.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.jdbcquery.DaoException;
import com.jdbcquery.JdbcConnector;

public class MySqlConnection implements JdbcConnector {

	public Connection getConnection() throws SQLException {
		try {
			final InitialContext ctx = new InitialContext();
			final Context envContext  = (Context) ctx.lookup("java:/comp/env");
			final DataSource ds = (DataSource)envContext.lookup("jdbc/OutlineDB");
			return ds.getConnection();
		} catch (final NamingException e) {
			throw new DaoException("Unable to perform jndi lookup");
		}
	}
}
```

### Sample service provider file
```
com.mydomain.dao.MySqlConnection
```

## License
[Beerware](http://en.wikipedia.org/wiki/Beerware)