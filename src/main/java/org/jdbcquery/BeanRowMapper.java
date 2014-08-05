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

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Handles converting a single row from a result set into a java bean of type T.
 *
 * @author Troy Histed
 *
 * @param <T>
 *            The java bean type that will be created with each row
 */
public abstract class BeanRowMapper<T> extends RowMapper<T> {

	protected Class<T> beanClass;

	private BeanRowMapper(Class<T> aBeanClass) {
		this.beanClass = aBeanClass;
	}

	/**
	 * Creates a BeanRowMapper.
	 */
	public BeanRowMapper() {
		super();
	}

	/**
	 * Creates a bean row mapper for a specific bean.
	 *
	 * <p>
	 * The class must have a no-arg constructor.
	 *
	 * @param aBeanClass
	 * @return a BeanRowMapper for the specified bean
	 */
	public static <T> BeanRowMapper<T> forClass(Class<T> aBeanClass) {
		return new BeanRowMapper<T>(aBeanClass) {
			@Override
			protected T newBeanInstance() {
				try {
					return this.beanClass.newInstance();
				} catch (final InstantiationException e) {
					throw new DaoException("Unable to create instance of " + this.beanClass.getName(), e);
				} catch (final IllegalAccessException e) {
					throw new DaoException("Unable to create instance of " + this.beanClass.getName(), e);
				}
			}
		};
	}

	/**
	 * Generate an instance of the bean.
	 *
	 * @return an instance of T
	 */
	protected abstract T newBeanInstance();

	/**
	 * Maps a single result set record to an instance of the query type.
	 *
	 * @param aResultSet
	 *            the result set record to process
	 * @return the mapped row
	 * @throws SQLException
	 *             the sql exception
	 */
	@Override
	protected T mapRow(ResultSet aResultSet) throws SQLException {
		int i = 1;
		try {
			final T bean = this.newBeanInstance();
			for (i = 1; i <= aResultSet.getMetaData().getColumnCount(); i++) {
				BeanUtils.setProperty(bean, aResultSet.getMetaData().getColumnLabel(i), aResultSet.getObject(i));
			}
			return bean;
		} catch (final IllegalAccessException e) {
			throw new DaoException("Error occurred setting bean property "
					+ aResultSet.getMetaData().getColumnLabel(i) + " with value " + aResultSet.getObject(i), e);
		} catch (final InvocationTargetException e) {
			throw new DaoException("Error occurred setting bean property "
					+ aResultSet.getMetaData().getColumnLabel(i) + " with value " + aResultSet.getObject(i), e);
		}
	}
}
