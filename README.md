jdbc-query
==========
[![Build Status](https://travis-ci.org/TroyHisted/jdbc-query.svg?branch=master)](https://travis-ci.org/TroyHisted/jdbc-query)

JDBC query builder that supports named parameters and reduces boilerplate code.

**IMPORTANT: This is beta software, it will have bugs. Do NOT use in a mission critical environment.**

## Features
* Named parameters
* Automatic housekeeping
* Removes boilerplate
* Works with POJOs
* No dependencies

## Example

```java
import java.util.Date;

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
	
	public long insertEmployee(String name, Date hireDate) {
		return Query.update("INSERT INTO people(name, hired) VALUES(:name, :hireDate)")
				.set("name", name)
				.set("hireDate", hireDate)
				.executeAndReturnKey();
	}
}

```

## Configuration
1. Create a class that implements _JdbcConnection_.
  * This class must implement the `getConnection()` method that simply returns a `java.sql.Connection`.
  * This class must implement the `getName()` method that returns a custom name for the connection. This may 
  be useful if there are multiple databases.
2. Expose the class as an SPI service.
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

	public Connection getConnection() {
		try {
			final InitialContext ctx = new InitialContext();
			final Context envContext  = (Context) ctx.lookup("java:/comp/env");
			final DataSource ds = (DataSource)envContext.lookup("jdbc/TestDB");
			return ds.getConnection();
		} catch (final NamingException e) {
			throw new DaoException("Unable to perform jndi lookup", e);
		} catch (final SQLException e) {
			throw new DaoException("Unable to establish connection", e);
		}
	}
	
	public String getName() {
		return "TEST_DATABASE";
	}
}
```

### Sample service provider file
```
com.mydomain.dao.MySqlConnection
```

## Usage
### Creating a Statement
A Statement consists of either an Update or Select and is created by either using the generic constructor or one 
of the specialized static constructors from the Query class. Either way works fine however the static methods 
reduce the need for specifying as many generics. Most of the static Select constructors also automatically 
configure a RowMapper of the static method return type. 

Constructor via _new_:
```java
Select<String> statement = new Select<String>("SELECT name FROM table", _RowMapper_);
```
(I'll describe what _RowMapper_ is later)

Constructor via static method:
```java
Select<String> statement = Query.forString("SELECT name FROM table");
```
(The static `forString` method includes a default _RowMapper_ for strings)

### Specify a database (optional)
If you've configured more than one JdbcConnector you'll need to define which connection to use for each 
Statement. To do this, simply add the connection name as the last argument to any of the Statement constructors
or to the static constructors. The String must match the string that is returned from the getName method of the 
corresponding JdbcConnector. 

```java
Select<String> statement = new Select<String>("SELECT name FROM table", _RowMapper_, "TEST_DATABASE");
Select<String> statement = Query.forString("SELECT name FROM table", "TEST_DATABASE");
```
Note: _If there are multiple JdbcConnectors and you do not specify the connection name to use, the first one 
declared in the service provider file will be used. This can be used to avoid specifying the connectionName for
one of the connections._

### Parameter names
Parameters are used in the sql statement with the syntax `:paramName`, where _paramName_ is the name of your 
parameter. Parameter names can consist of any characters except spaces, commas, or right parens. Coincidentally, 
after each parameter name there must be either a space, comma, or right paren (to indicate the end of the 
parameter name).

```sql
SELECT lastName FROM people WHERE firstName = UPPERCASE(:firstName)
```  

### Parameter values
Assigning values to the parameters is done on the query object through the many _setX_ methods. The parameter 
name is the first argument followed by the value. 

```java
statement.set("firstName", "john");
```

Note: _If a parameter name is declared multiple times in the query, the value only needs to be set once. _

### RowMappers
A row mapper defines how a single row from a result set maps to an object. This is basically where you
define how to extract the data from the result set in to your object. The simplest way to create a custom 
row mapper is by defining an anonymous class that implements the abstract `mapRow(ResultSet)` method.
```java
RowMapper<Person> rowMapper = new RowMapper<Person>(){
	protected Person mapRow(ResultSet resultSet) throws SQLException {
		Person person = new Person();
		person.setName(resultSet.getString("firstName"));
		return person;
	}};
```
### Executing a Select statement
There are two methods for running the select, `execute()` and `executeAll()`. The `execute()` method 
will run the select and use the _RowMapper_ to create and return the object created from the first row of the 
ResultSet. The `executeAll()` method will map each row to an Object and return them in a list.

```java
Person person = query.execute();
```
-or-
```java
List<Person> people = query.executeAll();
```

### Executing an Update statement
There are four methods for running the update, `execute()`, `executeAndReturnKey()`, 
`executeBatch()` and `executeBatchAndReturnKeys()`. The `execute()` and `executeBatch()` 
methods will execute the statement or batch of statements and return the number of records that were updated.
The `...AndReturnKey` methods will return any auto-generated keys instead of the number of records updated.
The keys will only be available for _INSERT_ statements where one of the columns is set to auto-increment.

```java
Query.update("DELETE FROM people WHERE personId = :personId").set("personId", 42).execute();
```

```java
Update update = Query.update(
	"INSERT INTO people(name, birthDate, description) VALUES(:name, :birthDate, :description)");

for (int i = 0; i < 10; i++) {
	update.set("name", "person_" + i);
	update.set("birthDate", new Date());
	update.set("description", (String) null);
	update.addBatch();
}

final long[] keys = update.executeBatchAndReturnKeys();
```

## License
[Beerware](http://en.wikipedia.org/wiki/Beerware)

### TODO
* Add the remaining setX methods
* Improve the connection clean up
* Write more tests