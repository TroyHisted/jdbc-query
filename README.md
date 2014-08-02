jdbc-query
==========

JDBC query builder supporting named parameters and a more concise syntax


## Features
* Removes boilerplate
* Named parameters using colon
* Automatic connection housekeeping

## Sample

```
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jdbcquery.Query;
import com.jdbcquery.RowMapper;

public class EmployeeDao {

	public String getName(int employeeId) {
		return Query.forString(" SELECT name FROM employees WHERE empId = :employeeId")
				.set("employeeId", employeeId)
				.execute();
	}
	
	public Date getHireDate(int employeeId) {
		return Query.forObject(
				" SELECT hiredate FROM employees WHERE employeeId = :employeeId",
				new RowMapper<Date>(){
					@Override
					protected Date mapRow(ResultSet resultSet) throws SQLException {
						return resultSet.getDate("hiredate");
				}})
			.set("employeeId", employeeId)
			.execute();
	}
}
```
 