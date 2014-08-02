jdbc-query
==========

JDBC query builder supporting named parameters and a more concise syntax

## Features
* Removes boilerplate
* Named parameters using colon
* Automatic connection housekeeping

## Sample

```java
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jdbcquery.Query;
import com.jdbcquery.RowMapper;

public class EmployeeDao {

	public String getName(int employeeId) {
		return Query.forString("SELECT name FROM employees WHERE empId = :employeeId")
				.set("employeeId", employeeId)
				.execute();
	}
	
	public Date getHireDate(int employeeId) {
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

## Requirements
* Create a class that implements _JdbcConnection_.
* Expose the class as an SPI service.
** Create a file called _com.jdbcquery.JdbcConnector_ in your _META-INF_ folder.
*** In that file specify the fully qualified name of your _JdbcConnection_ implementation.

## License
[Beerware](http://en.wikipedia.org/wiki/Beerware)