jdbc-query
==========
[![Build Status](https://travis-ci.org/TroyHisted/jdbc-query.svg?branch=master)](https://travis-ci.org/TroyHisted/jdbc-query)

Java SQL statement builder that supports named parameters and reduces JDBC boilerplate code.

## Features
* Resource management
* Exception handling
* Named parameters in SQL
* Supports POJOs and JavaBeans

## Examples

```java
import java.util.Date;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbcquery.Query;
import org.jdbcquery.RowMapper;

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
  * Create a file called _org.jdbcquery.JdbcConnector_ in your _META-INF_ folder.
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

import org.jdbcquery.DaoException;
import org.jdbcquery.JdbcConnector;

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
Assigning values to the parameters is done on the Statement object through the many _setX_ methods. The parameter 
name is the first argument, followed by the value. 

```java
statement.set("firstName", "john");
```
Note: _If a parameter name is declared multiple times in the query, the value only needs to be set once._

The `setBean(Object)` method is a special setter that takes a JavaBean and sets any of its properties into 
the statement. So invoking setBean on a Person object that has a "name" property would be equivalent to calling
set("name", person.getName()). Only the properties that actually match a parameter name will be set. 
```java
final Integer count = Query.forInteger(
		"SELECT count(*) "
		+ " FROM geo_location "
		+ " WHERE zip = :zip"
		+ "   AND stateAbbreviation = :stateAbbreviation"
		+ "   AND city = :city"
		+ "   AND latitude = :coordinates.latitude"
		+ "   AND longitude = :coordinates.longitude")
	.setBean(location)
	.execute();
```
Note: _This uses BeanUtils which doesn't handle null object references for nested properties very well, so use
the nested property syntax with caution._

### RowMapper
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

### BeanRowMapper
A bean row mapper is a specialized row mapper that uses BeanUtils to map the result set to the object by
treating the column names as bean properties. The BeanRowMapper comes with an implementation of 
`mapRow(ResultSet)`, but requires the implementation of a `newBeanInstance()` method which should
return a new instance of the bean you're trying to map to. The BeanRowMapper also includes a convenient static 
`forClass(Class<T>)` method that creates a special BeanRowMapper using the getInstance() method of the
specified class to implement the newBeanInstance() method. The Query class even provides a shorthand way to 
create the row mapper through the `forBean(String, Class)` method which will create the BeanRowMapper for 
the specified class.

```java
return Query.forBean(
		"    SELECT "
		+ "    city, "
		+ "    stateAbbreviation, "
		+ "    zip, "
		+ "    latitude AS 'coordinates.latitude', "
		+ "    longitude AS 'coordinates.longitude' "
		+ "  FROM geo_location "
		+ "  WHERE zip = :zip",
		Location.class)
	.set("zip", aZipCode)
	.execute();
```

Note: _Since this uses BeanUtils, it supports setting nested properties(as in the coordinates.latitude in the 
example), however the label must be wrapped in single quotes because the dot character would otherwise render
the sql invalid._

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

#### Cursor movement
By default, when invoking an execute method, the cursor of the result set will be moved before the row mapper
is called. This behavior can be modified by passing `false` to either of the execute methods. Under this 
condition it is up to the row mapper to move the cursor. This is useful if you need more control over how a
object is constructed, if, for example, you need to map multiple rows to a single object. The execute method
will still only invoke mapRow() once, while executeForAll will repeatedly call mapRow() until the cursor of the
result set is past the last item.   

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
[Apache License, Version 2.0](http://opensource.org/licenses/Apache-2.0)

  